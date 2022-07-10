package io.github.devrawr.practice.kit.queue.menu

import io.github.devrawr.practice.kit.KitService
import io.github.devrawr.practice.kit.queue.Queue
import io.github.devrawr.practice.match.MatchService
import io.github.devrawr.practice.match.MatchType
import io.github.devrawr.practice.match.team.MatchTeam
import io.github.devrawr.tasks.Tasks
import io.github.nosequel.menu.Menu
import io.github.nosequel.menu.buttons.Button
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class QueueMenu(
    parent: Player,
    val team: MatchTeam,
    val type: MatchType
) : Menu(parent, "", 18)
{
    /**
     * The method to get the buttons for the current inventory tick
     *
     *
     * Use `this.buttons[index] = Button` to assign
     * a button to a slot.
     */
    override fun tick()
    {
        for (withIndex in KitService.kits.withIndex())
        {
            val kit = withIndex.value
            val index = withIndex.index

            buttons[index] = QueueButton(
                team, kit.retrieveQueueOfType(type)
            )
        }
    }
}

class QueueButton(team: MatchTeam, queue: Queue) : Button(queue.kit.icon)
{
    init
    {
        this.displayName = "${ChatColor.GREEN}${queue.kit.displayName}"
        this.lore = arrayOf(
            "",
            "${ChatColor.WHITE}In Queue: ${ChatColor.AQUA}${queue.entries.size}",
            "${ChatColor.WHITE}In Fights: ${ChatColor.AQUA}${
                MatchService
                    .matches
                    .filter {
                        it.value.kit == queue.kit && it.value.matchType == queue.type
                    }.count()
            }",
            "",
            "${ChatColor.YELLOW}Click to queue."
        )

        this.setClickAction { event ->
            event
                .whoClicked
                .closeInventory()

            val match = queue.queue(
                team
            )

            if (match != null)
            {
                match.execute {
                    it.sendMessage("${ChatColor.GREEN}Opponent found!")
                }

                Tasks
                    .sync()
                    .delay(40L) {
                        match.start()
                    }
            }
        }
    }
}