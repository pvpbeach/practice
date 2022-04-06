package io.github.devrawr.practice.arena.generation

import org.bukkit.DyeColor
import org.bukkit.Location
import org.bukkit.Material

val TEMPLATE_ARENA by lazy {
    val blocks = mutableListOf<ArenaBlockData>()

    for (x in -25..25)
    {
        for (z in -25..25)
        {
            blocks.add(
                ArenaBlockData(
                    x = x.toDouble(),
                    z = z.toDouble(),
                    y = 10.0,
                    type = Material.STAINED_CLAY,
                    data = DyeColor.GREEN.data
                )
            )

            blocks.add(
                ArenaBlockData(
                    x = x.toDouble(),
                    z = z.toDouble(),
                    y = 40.0,
                    type = Material.GLASS,
                    data = 0
                )
            )
        }
    }

    return@lazy ArenaBuild(blocks)
}

class ArenaBuild(val blocks: List<ArenaBlockData>)
{
    fun generateAt(location: Location)
    {
        for (block in blocks)
        {
            val loc = location.clone().add(block.x, block.y, block.z)
            val type = block.type

            loc.block.type = type
            loc.block.data = block.data
        }
    }
}

data class ArenaBlockData(
    val x: Double,
    val y: Double,
    val z: Double,
    val type: Material,
    val data: Byte
)