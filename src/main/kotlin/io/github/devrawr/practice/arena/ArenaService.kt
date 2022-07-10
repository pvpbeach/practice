package io.github.devrawr.practice.arena

import io.github.devrawr.practice.kit.Kit
import org.bukkit.Bukkit
import org.bukkit.Location

val TEMPLATE_ARENA = Arena("test-arena").apply {
    this.build = io.github.devrawr.practice.arena.generation.TEMPLATE_ARENA

    this.firstLocation = Location(
        Bukkit.getWorlds()[0], 224.0, 211.0, 196.0
    )

    this.secondLocation = Location(
        Bukkit.getWorlds()[0], 175.0, 211.0, 197.0
    )

    this.buildFromLocation = Location(
        Bukkit.getWorlds()[0], 200.0, 200.0, 200.0
    )

    this.appliedKitIds += "test"
    this.appliedKitIds += "mommy"
}

object ArenaService
{
    val arenas = mutableListOf<Arena>(
        TEMPLATE_ARENA
    )

    fun getRandomArena(kit: Kit? = null): Arena?
    {
        var random = arenas.randomOrNull()

        if (random != null
            && kit != null
            && !random.appliedKitIds.contains(kit.id)
        )
        {
            random = this.getRandomArena(kit)
        }

        return random
    }
}