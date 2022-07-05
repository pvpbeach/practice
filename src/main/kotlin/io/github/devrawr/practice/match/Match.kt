package io.github.devrawr.practice.match

import io.github.devrawr.practice.arena.Arena
import io.github.devrawr.practice.extensions.retrieveProfile
import io.github.devrawr.practice.kit.Kit
import io.github.devrawr.practice.match.event.type.MatchCreateEvent
import io.github.devrawr.practice.match.event.type.MatchEndEvent
import io.github.devrawr.practice.match.event.type.MatchPlayerDeathEvent
import io.github.devrawr.practice.match.event.type.MatchStartEvent
import io.github.devrawr.practice.match.team.MatchTeam
import io.github.devrawr.practice.match.tracking.TrackedBlock
import io.github.devrawr.practice.match.tracking.TrackedBlockType
import io.github.devrawr.practice.player.PlayerState
import io.github.devrawr.tasks.Tasks
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import java.util.*

@Suppress("DEPRECATION")
class Match(
    val firstTeam: MatchTeam,
    val secondTeam: MatchTeam,
    val kit: Kit,
    val arena: Arena,
    var matchType: MatchType = MatchType.Solo
)
{
    var state = MatchState.Unknown

    val trackedBlocks = mutableListOf<TrackedBlock>()
    val preparedArena = arena.nextPreparedArena

    init
    {
        execute { team ->
            team.execute {
                MatchService.matches[it] = this
            }
        }

        Bukkit
            .getPluginManager()
            .callEvent(
                MatchCreateEvent(
                    this
                )
            )
    }

    fun start()
    {
        this.state = MatchState.Starting

        execute { team ->
            // wanna do this first, considering it clears the player's inventory.
            team.execute {
                it.retrieveProfile()
                    .state = PlayerState.Match
            }

            // equip the kit for the team
            kit.defaultLayout.equip(team)

            // teleport the team to their designated location
            preparedArena.teleport(
                team, this
            )
        }

        var current = 6

        Tasks
            .async()
            .repeating(0L, 20) {
                current--

                execute {
                    it.sendMessage("${ChatColor.YELLOW}${current}...")

                    if (current <= 1)
                    {
                        state = MatchState.Started
                    }
                }
            }.cancelAfter(20 * 5)

        Tasks
            .async()
            .delay(20 * 5) {
                Bukkit.getPluginManager().callEvent(
                    MatchStartEvent(
                        this
                    )
                )
            }
    }

    fun death(id: UUID)
    {
        val team = if (firstTeam.ids.contains(id))
        {
            firstTeam
        } else if (secondTeam.ids.contains(id))
        {
            secondTeam
        } else
        {
            throw IllegalArgumentException("$id is not a part of this match.")
        }

        Bukkit.getPluginManager().callEvent(
            MatchPlayerDeathEvent(
                match = this,
                diedId = id,
                diedTeam = team,
            )
        )

        team.death(id)

        if (team.retrieveAlive().isEmpty())
        {
            this.endGame(
                winner = if (team == this.firstTeam)
                {
                    secondTeam
                } else
                {
                    firstTeam
                },
                loser = team
            )
        }
    }

    fun endGame(winner: MatchTeam, loser: MatchTeam)
    {
        Bukkit.getPluginManager().callEvent(
            MatchEndEvent(
                this,
                winner = winner,
                loser = loser
            )
        )
        // this doesn't completely clear the arena, just sets it's state to not being used, and clears all placed blocks within the match.
        this.preparedArena.destruct()

        execute { team ->
            team.execute {
                MatchService.matches.remove(it)

                it.retrieveProfile(false)
                    .state = PlayerState.Lobby
            }
        }
    }

    fun execute(action: (MatchTeam) -> Unit)
    {
        action(firstTeam)
        action(secondTeam)
    }

    fun trackBlock(
        location: Location,
        trackedBlockType: TrackedBlockType = if (location.block.type == Material.AIR)
        {
            TrackedBlockType.Break
        } else
        {
            TrackedBlockType.Place
        }
    )
    {
        this.trackedBlocks.add(
            TrackedBlock(location, trackedBlockType)
        )
    }

    fun trackBlock(
        block: Block,
        trackedBlockType: TrackedBlockType = if (block.type == Material.AIR)
        {
            TrackedBlockType.Break
        } else
        {
            TrackedBlockType.Place
        }
    ) = trackBlock(block.location, trackedBlockType)

    fun isTrackedBlock(location: Location): Boolean
    {
        return this.trackedBlocks.any {
            it.location.distance(location) <= 0
        }
    }

    fun sendMessage(message: String)
    {
        execute {
            it.sendMessage(message)
        }
    }
}