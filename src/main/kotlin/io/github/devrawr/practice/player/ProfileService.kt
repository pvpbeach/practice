package io.github.devrawr.practice.player

import java.util.*

object ProfileService
{
    private val profiles = hashMapOf<UUID, Profile>()

    fun retrieveProfile(id: UUID): Profile
    {
        return profiles[id]
            ?: Profile(id).apply {
                profiles[id] = this
            }
    }
}