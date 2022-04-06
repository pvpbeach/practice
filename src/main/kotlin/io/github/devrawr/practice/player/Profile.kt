package io.github.devrawr.practice.player

import io.github.devrawr.practice.kit.KitService
import io.github.devrawr.practice.kit.queue.Queue
import io.github.devrawr.practice.match.Match
import io.github.devrawr.practice.match.MatchService
import io.github.devrawr.practice.player.layout.LayoutService
import org.bukkit.Bukkit
import java.lang.IllegalStateException
import java.util.*

class Profile(val id: UUID)
{
    val match: Match?
        get()
        {
            return MatchService.matches[this.id]
        }

    val queue: Queue?
        get()
        {
            return KitService.queueCache[this.id]
        }

    var state: PlayerState = PlayerState.Lobby
        get()
        {
            field = when
            {
                field == PlayerState.Match && match == null -> PlayerState.Lobby
                field == PlayerState.Queue && queue == null -> PlayerState.Lobby
                else -> PlayerState.Lobby
            }

            return field
        }
        set(value)
        {
            field = value

            val layout = LayoutService.retrieveByState(value)
            val player = Bukkit.getPlayer(this.id)
                ?: throw IllegalStateException("No online player found with id ${this.id}")

            layout.equip(player)
        }
}

enum class PlayerState
{
    Lobby,
    Queue,
    Match
}