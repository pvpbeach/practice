package io.github.devrawr.practice.kit

import io.github.devrawr.practice.extensions.player
import io.github.devrawr.practice.kit.queue.Queue
import io.github.devrawr.practice.match.MatchType
import io.github.devrawr.practice.match.team.MatchTeam
import io.github.devrawr.practice.match.team.type.LiveMatchTeam
import io.github.devrawr.practice.match.team.type.PointMatchTeam
import org.bukkit.Material
import java.util.*

class Kit(val id: String)
{
    var icon = Material.DIAMOND_SWORD
    var displayName = id.capitalize()
    var defaultLayout: KitLayout = KitLayout("Default")
    var flags = mutableListOf<KitFlag>()

    val queues = hashMapOf<MatchType, Queue>()

    fun retrieveQueueOfType(type: MatchType): Queue
    {
        if (!queues.containsKey(type))
        {
            queues[type] = Queue(this, type)
        }

        return queues[type]!!
    }

    @Suppress("DEPRECATION")
    fun createTeamFromIds(ids: List<UUID>): MatchTeam
    {
        if (this.flags.contains(KitFlag.LifeKit) && this.flags.contains(KitFlag.PointKit))
        {
            throw IllegalArgumentException("Kit has both LiveKit and PointKit flags, but should only have one of them.")
        }

        val map = ids
            .associateWith { it.player?.isOnline == true } // all players should be alive at this point, so associate them by true.
            .toMutableMap()

        // might have to change this method if more MatchTeam specific-kit-types get added.
        return if (this.flags.contains(KitFlag.LifeKit))
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
    BreakAll,
    DespawnBlock,

    LifeKit,
    PointKit
}