package io.github.devrawr.practice.player.layout

import io.github.devrawr.practice.util.ItemWrapper
import org.bukkit.entity.Player

open class Layout(
    val id: String,
    val items: List<ItemWrapper> = emptyList()
)
{
    open fun equip(player: Player)
    {
        player.inventory.clear()
        player.inventory.armorContents = null
        player.updateInventory()

        player.sendMessage("cleared ur inv lol")

        for (item in items)
        {
            item.giveToPlayer(player)
        }
    }
}