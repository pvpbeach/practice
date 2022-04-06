package io.github.devrawr.practice.kit.queue

import io.github.devrawr.practice.arena.ArenaService
import io.github.devrawr.practice.kit.Kit
import io.github.devrawr.practice.kit.KitService
import io.github.devrawr.practice.match.Match
import io.github.devrawr.practice.match.MatchType
import io.github.devrawr.practice.match.team.MatchTeam
import io.github.devrawr.practice.player.PlayerState
import io.github.devrawr.practice.player.ProfileService
import org.bukkit.Bukkit

class Queue(val kit: Kit, val type: MatchType)
{
    val entries = mutableListOf<MatchTeam>()

    fun queue(team: MatchTeam): Match?
    {
        this.entries.add(team)

        team.execute {
            KitService.queueCache[it] = this

            ProfileService
                .retrieveProfile(it)
                .state = PlayerState.Queue
        }

        if (this.entries.size >= 2)
        {
            // to whoever is seeing this in the future, no. this is not taking the first one twice.
            // this is getting the first and second one as the names indicate, because the first one gets removed.
            // if it's removed - it's not the first one anymore, meaning the second can be retrieved from the first index.
            val first = this.entries.removeFirst()
            val second = this.entries.removeFirst()

            // clear the ids from queueCache
            listOf(first, second).forEach { current ->
                current.execute {
                    KitService.queueCache.remove(it)
                }
            }

            return Match(
                first, second,
                kit = kit,
                arena = ArenaService.getRandomArena(kit)!!,
                matchType = type
            )
        }

        return null
    }
}