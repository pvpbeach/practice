package io.github.devrawr.practice.arena.generation

import io.github.devrawr.practice.arena.Arena
import io.github.devrawr.practice.extensions.player
import io.github.devrawr.practice.match.Match
import io.github.devrawr.practice.match.team.MatchTeam
import org.bukkit.Material

class PreparedArena(
    val arena: Arena,
    val offsetX: Int, // this will probably the only offset used. we can make every arena be on a z-grid, so every single arena-type is on a single row.
    val offsetZ: Int // read comment above, this might not get used. we just have this in case.
)
{
    var currentMatch: Match? = null
        get()
        {
            println("hi!!! $field")
            return field
        }

    // NOTE: despite its name, this should not be a finalize() block.
    fun destruct()
    {
        val match = currentMatch

        if (match != null)
        {
            for (block in match.trackedBlocks)
            {
                block
                    .location
                    .block
                    .type = Material.AIR
            }

//            this.currentMatch = null
        }
    }

    fun teleport(team: MatchTeam, match: Match)
    {
        this.teleport(
            team = team,
            index = if (match.firstTeam == team)
            {
                0
            } else
            {
                1
            }
        )
    }

    fun teleport(team: MatchTeam, index: Int)
    {
        println(index)
        val location = if (index == 0)
        {
            arena.firstLocation
        } else
        {
            arena.secondLocation
        }

        if (location != null)
        {
            team.execute {
                it.player?.teleport(
                    location.clone()
                        .add(
                            offsetX.toDouble(),
                            0.0,
                            offsetZ.toDouble()
                        )
                )
            }
        }
    }
}