package io.github.devrawr.practice.match.team.type

import io.github.devrawr.practice.match.team.MatchTeam
import java.util.*

@Suppress("DEPRECATION")
@Deprecated("Should use Kit#createTeamFromIds() for more accurate instantiation, as it creates match team of specific type.")
class LiveMatchTeam(
    ids: MutableMap<UUID, Boolean>,
    private val liveStart: Int = 5
) : MatchTeam(ids)
{
    private val lives = hashMapOf<UUID, Int>()

    init
    {
        ids.keys.forEach {
            lives[it] = liveStart
        }
    }

    override fun death(id: UUID)
    {
        var lives = lives[id]
            ?: throw IllegalArgumentException("$id is not a part of team ${this.hashCode()}")

        this.lives[id] = lives--

        if (lives <= 0)
        {
            ids[id] = false // set the player to "dead".
        }
    }
}