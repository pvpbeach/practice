package io.github.devrawr.practice.match

import io.github.devrawr.practice.arena.Arena
import io.github.devrawr.practice.kit.Kit
import io.github.devrawr.practice.match.team.MatchTeam
import io.github.devrawr.practice.match.tracking.TrackedBlock
import io.github.devrawr.practice.match.tracking.TrackedBlockType
import io.github.devrawr.tasks.Tasks
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block

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

    fun start()
    {
        execute {
            // equip the kit for the team
            kit.defaultLayout.equip(it)

            // teleport the team to their designated location
            preparedArena.teleport(
                it, this
            )
        }

        var current = 6

        Tasks
            .async()
            .repeating(0L, 20) {
                execute {
                    it.sendMessage("${ChatColor.YELLOW}${current--}...")

                    if (current <= 0)
                    {
                        state = MatchState.Started
                    }
                }
            }.cancelAfter(20 * 5)
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
}