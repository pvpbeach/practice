package io.github.devrawr.practice.extensions

import io.github.devrawr.practice.player.Profile
import io.github.devrawr.practice.player.ProfileService
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

val UUID.player: Player?
    get()
    {
        return Bukkit.getPlayer(this)
    }

val UUID.profile: Profile?
    get()
    {
        return ProfileService.profiles[this] // don't call retrieveProfile.
    }

fun UUID.retrieveProfile(query: Boolean = true): Profile
{
    return ProfileService.retrieveProfile(this, query)
}