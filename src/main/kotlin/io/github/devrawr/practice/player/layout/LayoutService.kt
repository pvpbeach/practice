package io.github.devrawr.practice.player.layout

import io.github.devrawr.practice.kit.KitService
import io.github.devrawr.practice.match.MatchType
import io.github.devrawr.practice.player.PlayerState
import io.github.devrawr.practice.util.ItemWrapper
import org.bukkit.ChatColor
import org.bukkit.Material

val LOBBY_LAYOUT = Layout(
    "lobby-layout",
    listOf(
        ItemWrapper(Material.IRON_SWORD)
            .index(0)
            .displayName("${ChatColor.YELLOW}${ChatColor.BOLD}Unranked Queue")
            .action {
                val player = it.player
                val kit = KitService.kits.first()

                // add player to queue... temporary for testing!
                val queue = kit
                    .retrieveQueueOfType(MatchType.Solo)

                if (queue.entries.firstOrNull
                    {
                        it.ids.containsKey(player.uniqueId)
                    } == null
                )
                {

                    val match = queue.queue(
                        kit.createTeamFromIds(
                            listOf(player.uniqueId)
                        )
                    )

                    player.sendMessage("added to queue - ${queue.entries.size}")

                    if (match != null)
                    {
                        match.start()
                        player.sendMessage("match started")
                    }
                }
            }
    )
)

val MATCH_LAYOUT = Layout("match-layout")

object LayoutService
{
    private val layouts = hashMapOf(
        "lobby-layout" to LOBBY_LAYOUT,
        "match-layout" to MATCH_LAYOUT
    )

    private val profileStateLayouts = hashMapOf(
        PlayerState.Lobby to "lobby-layout",
        PlayerState.Queue to "queue-layout",
        PlayerState.Match to "match-layout"
    )

    fun retrieveByState(state: PlayerState): Layout
    {
        return layouts[profileStateLayouts[state]!!] ?: Layout("none")
    }
}