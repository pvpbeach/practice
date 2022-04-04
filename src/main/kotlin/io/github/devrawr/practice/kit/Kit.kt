package io.github.devrawr.practice.kit

import io.github.devrawr.practice.match.team.MatchTeam
import io.github.devrawr.practice.match.team.type.LiveMatchTeam
import io.github.devrawr.practice.match.team.type.PointMatchTeam
import org.bukkit.Material
import java.util.*

class Kit(val id: String)
{
    var icon = Material.DIAMOND_SWORD
    var displayName = "${id}-display"
    var defaultLayout: KitLayout = KitLayout("Default")
    var flags = mutableListOf<KitFlag>()

    @Suppress("DEPRECATION")
    fun createTeamFromIds(ids: List<UUID>): MatchTeam
    {
        if (this.flags.contains(KitFlag.LiveKit) && this.flags.contains(KitFlag.PointKit))
        {
            throw IllegalArgumentException("Kit has both LiveKit and PointKit flags, but should only have one of them.")
        }

        val map = ids
            .associateWith { true } // all players should be alive at this point, so associate them by true.
            .toMutableMap()

        // might have to change this method if more MatchTeam specific-kit-types get added.
        return if (this.flags.contains(KitFlag.LiveKit))
        {
            LiveMatchTeam(map)
        } else if (this.flags.contains(KitFlag.PointKit))
        {
            PointMatchTeam(map)
        } else
        {
            MatchTeam(map)
        }
    }
}

enum class KitFlag
{
    Build,

    LiveKit,
    PointKit
}