package io.github.devrawr.practice.arena

import io.github.devrawr.practice.kit.Kit

object ArenaService
{
    val arenas = mutableListOf<Arena>()

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