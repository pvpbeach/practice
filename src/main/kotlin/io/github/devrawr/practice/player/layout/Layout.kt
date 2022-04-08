package io.github.devrawr.practice.player.layout

import io.github.devrawr.practice.util.ItemWrapper
import org.bukkit.Location
import org.bukkit.entity.Player

open class Layout(
    val id: String,
    val items: List<ItemWrapper> = emptyList(),
    val spawnLocation: Location? = null
)
{
    open fun equip(player: Player)
    {
        player.inventory.clear()
        player.inventory.armorContents = null
        player.updateInventory()

        for (item in items)
        {
            item.giveToPlayer(player)
        }

        if (spawnLocation != null)
        {
            player.teleport(spawnLocation)
        }
    }
}