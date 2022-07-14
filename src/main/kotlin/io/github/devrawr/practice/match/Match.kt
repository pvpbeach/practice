package io.github.devrawr.practice.match

import io.github.devrawr.practice.arena.Arena
import io.github.devrawr.practice.extensions.retrieveProfile
import io.github.devrawr.practice.kit.Kit
import io.github.devrawr.practice.kit.KitFlag
import io.github.devrawr.practice.match.event.type.MatchCreateEvent
import io.github.devrawr.practice.match.event.type.MatchEndEvent
import io.github.devrawr.practice.match.event.type.MatchPlayerDeathEvent
import io.github.devrawr.practice.match.event.type.MatchStartEvent
import io.github.devrawr.practice.match.team.MatchTeam
import io.github.devrawr.practice.match.team.type.BedRespawnMatchTeam
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
        this.preparedArena.currentMatch = this

        execute { team ->
            // wanna do this first, considering it clears the player's inventory.
            team.match = this
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
        val team = getTeam(id)

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

        if (kit.flags.contains(KitFlag.DespawnBlock))
        {
            Tasks
                .sync()
                .delay(20 * 5L) {
                    location.block.type = Material.AIR
                }
        }
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

    fun getTeam(id: UUID): MatchTeam
    {
        return if (firstTeam.ids.contains(id))
        {
            firstTeam
        } else if (secondTeam.ids.contains(id))
        {
            secondTeam
        } else
        {
            throw IllegalArgumentException("$id is not a part of this match.")
        }
    }


    fun getBedTeam(location: Location): BedRespawnMatchTeam?
    {
        if (location.block.type != Material.BED || firstTeam !is BedRespawnMatchTeam || secondTeam !is BedRespawnMatchTeam)
        {
            return null
        }

        val firstBedLocation = arena.firstBedLocation?.let {
            preparedArena.getScaledLocation(it)
        }

        val secondBedLocation = arena.secondBedLocation?.let {
            preparedArena.getScaledLocation(it)
        }

        if (firstBedLocation == null || secondBedLocation == null)
        {
            return null
        }

        if (firstBedLocation.distanceSquared(location) < 1)
        {
            return firstTeam
        } else if (secondBedLocation.distanceSquared(location) < 1)
        {
            return secondTeam
        }

        return null
    }
}