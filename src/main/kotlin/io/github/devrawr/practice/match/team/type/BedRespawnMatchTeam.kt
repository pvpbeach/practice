package io.github.devrawr.practice.match.team.type

import io.github.devrawr.practice.match.team.MatchTeam
import org.bukkit.Bukkit
import java.util.*

class BedRespawnMatchTeam
@Suppress("DEPRECATION")
@Deprecated(
    "Should use Kit#createTeamFromIds() for more accurate instantiation, as it creates match team of specific type",
    ReplaceWith(
        "kit.createTeamFromIds(ids)"
    )
)
constructor(ids: MutableMap<UUID, Boolean>) : MatchTeam(ids)
{
    var destroyedBed: Boolean = false

    override fun death(id: UUID)
    {
        if (!ids.containsKey(id))
        {
            return
        }

        if (destroyedBed)
        {
            ids[id] = false // set the player to "dead".
        }

        val match = match

        Bukkit.getPlayer(id)?.let {
            match
                ?.preparedArena
                ?.teleport(it, match)
        }
    }
}