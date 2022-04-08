package io.github.devrawr.practice.player

import io.github.nosequel.data.DataHandler
import io.github.nosequel.data.DataStoreType
import java.util.*

object ProfileService
{
    private val repository = DataHandler
        .createStoreType<UUID, Profile>(
            DataStoreType.REDIS // yep. we're using redis for storing. want reasoning? feel free to reach out to me on discord: string#0365
        )

    val profiles = hashMapOf<UUID, Profile>()

    fun retrieveProfile(id: UUID, query: Boolean = true): Profile
    {
        return profiles[id] ?: kotlin.run {
            if (query)
            {
                repository.retrieve(id) ?: Profile(id)
            } else
            {
                Profile(id)
            }
        }
    }
}