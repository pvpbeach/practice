package io.github.devrawr.practice.arena

import io.github.devrawr.practice.arena.generation.ArenaBuild
import io.github.devrawr.practice.arena.generation.PreparedArena
import io.github.devrawr.practice.arena.generation.TEMPLATE_ARENA
import org.bukkit.Location

class Arena(val id: String)
{
    var build: ArenaBuild = TEMPLATE_ARENA

    var firstLocation: Location? = null
    var secondLocation: Location? = null
    var buildFromLocation: Location? = null

    var firstBedLocation: Location? = null
    var secondBedLocation: Location? = null

    val preparedArenas = mutableListOf<PreparedArena>()
    val appliedKitIds = mutableListOf<String>()

    val nextPreparedArena: PreparedArena
        get()
        {
            if (buildFromLocation == null)
            {
                throw IllegalStateException("buildFromLocation has not been set for this arena.")
            }

            var next = preparedArenas.firstOrNull {
                it.currentMatch == null
            }

            if (next == null)
            {
                println("generating new next")

                val offsetX = 200
                val offsetZ = 0

                val x = offsetX * (preparedArenas.size + 1)
                val z = offsetZ * (preparedArenas.size + 1)

                build.generateAt(
                    buildFromLocation!!.clone()
                        .add(
                            x.toDouble(),
                            0.0,
                            z.toDouble()
                        )
                )

                next = PreparedArena(
                    this,
                    x,
                    z
                )

                preparedArenas.add(next)
            }

            return next
        }
}