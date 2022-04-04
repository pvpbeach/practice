package io.github.devrawr.practice.match.team

import io.github.devrawr.practice.extensions.player
import org.bukkit.ChatColor
import java.util.*

open class MatchTeam(
    val ids: List<UUID>
)
{
    fun sendMessage(message: String)
    {
        execute {
            it.player?.sendMessage(
                ChatColor.translateAlternateColorCodes('&', message)
            )
        }
    }

    fun execute(action: (UUID) -> Unit)
    {
        for (id in ids)
        {
            action(id)
        }
    }

    fun retrieveFirst(): UUID?
    {
        return this.ids.firstOrNull()
    }
}