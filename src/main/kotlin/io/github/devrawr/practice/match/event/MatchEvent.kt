package io.github.devrawr.practice.match.event

import io.github.devrawr.practice.match.Match
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class MatchEvent(val match: Match) : Event()
{
    companion object
    {
        val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList
        {
            return HANDLERS
        }
    }

    val kit = match.kit
    val arena = match.arena
    val preparedArena = match.preparedArena

    override fun getHandlers(): HandlerList
    {
        return HANDLERS
    }
}