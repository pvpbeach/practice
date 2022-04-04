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

    val preparedArenas = mutableListOf<PreparedArena>()
    
    val nextPreparedArena: PreparedArena
        get()
        {
            if (buildFromLocation == null)
            {
                throw IllegalStateException("buildFromLocation has not been set for this arena.")
            }

            var next = preparedArenas.firstOrNull {
                it.currentMatch != null
            }

            if (next == null)
            {
                val offsetX = 200
                val offsetZ = 0

                build.generateAt(
                    buildFromLocation!!.clone()
                        .add(
                            offsetX.toDouble(),
                            0.0,
                            offsetZ.toDouble()
                        )
                )

                next = PreparedArena(
                    this,
                    offsetX,
                    offsetZ
                )
            }

            return next
        }
}