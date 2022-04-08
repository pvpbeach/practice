package io.github.devrawr.practice.listener

import io.github.devrawr.practice.extensions.retrieveProfile
import io.github.devrawr.practice.player.PlayerState
import io.github.devrawr.practice.player.ProfileService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object GeneralListener : Listener
{
    @EventHandler
    fun onJoin(event: PlayerJoinEvent)
    {
        val player = event.player
        val profile = player.uniqueId.retrieveProfile()

        profile.state = PlayerState.Lobby
    }
}