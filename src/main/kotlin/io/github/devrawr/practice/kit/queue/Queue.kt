package io.github.devrawr.practice.kit.queue

import io.github.devrawr.practice.arena.ArenaService
import io.github.devrawr.practice.kit.Kit
import io.github.devrawr.practice.match.Match
import io.github.devrawr.practice.match.MatchType
import io.github.devrawr.practice.match.team.MatchTeam

class Queue(val kit: Kit, val type: MatchType)
{
    val entries = mutableListOf<MatchTeam>()

    fun queue(team: MatchTeam): Match?
    {
        this.entries.add(team)

        if (this.entries.size >= 2)
        {
            // to whoever is seeing this in the future, no. this is not taking the first one twice.
            // this is getting the first and second one as the names indicate, because the first one gets removed.
            // if it's removed - it's not the first one anymore, meaning the second can be retrieved from the first index.
            val first = this.entries.removeFirst()
            val second = this.entries.removeFirst()

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