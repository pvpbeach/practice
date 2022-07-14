package io.github.devrawr.practice.match.team

import io.github.devrawr.practice.extensions.player
import io.github.devrawr.practice.match.Match
import org.bukkit.ChatColor
import java.util.*


open class MatchTeam
@Deprecated(
    "Should use Kit#createTeamFromIds() for more accurate instantiation, as it creates match team of specific type",
    ReplaceWith("kit.createTeamFromIds(ids)")
) constructor(val ids: MutableMap<UUID, Boolean>)
{
    var match: Match? = null

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
        for (id in ids.keys)
        {
            action(id)
        }
    }

    fun retrieveFirst(): UUID?
    {
        return this.ids.keys.firstOrNull()
    }

    fun retrieveAlive(): List<UUID>
    {
        return this.ids.filter {
            it.value
        }.keys.toList()
    }

    open fun death(id: UUID)
    {
        ids[id] = false
    }
}