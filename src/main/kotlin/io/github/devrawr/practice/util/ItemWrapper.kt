package io.github.devrawr.practice.util

import io.github.devrawr.events.Events
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemWrapper(val itemStack: ItemStack)
{
    constructor(material: Material) : this(
        ItemStack(material)
    )

    var index = -1

    fun displayName(name: String): ItemWrapper
    {
        return editMeta {
            it.displayName = ChatColor.translateAlternateColorCodes('&', name)
        }
    }

    fun lore(vararg lore: String): ItemWrapper
    {
        return editMeta {
            it.lore = lore.asList()
        }
    }

    fun lore(lore: List<String>): ItemWrapper
    {
        return editMeta {
            it.lore = lore
        }
    }

    fun action(action: (PlayerInteractEvent) -> Unit): ItemWrapper
    {
        return this.apply {
            Events
                .listenTo<PlayerInteractEvent>()
                .filter { it.item != null && it.item.isSimilar(this.itemStack) }
                .on(action)
        }
    }

    fun index(index: Int): ItemWrapper
    {
        return this.apply {
            this.index = index
        }
    }

    fun giveToPlayer(player: Player, action: ((PlayerInteractEvent) -> Unit)? = null)
    {
        if (action != null)
        {
            Events
                .listenTo<PlayerInteractEvent>()
                .filter { it.item != null && it.item.isSimilar(this.itemStack) }
                .filter { it.player == player }
                .on(action)
        }

        if (index == -1)
        {
            player.inventory.addItem(this.itemStack)
        } else
        {
            player.inventory.setItem(index, this.itemStack)
        }
    }

    fun editMeta(edit: (ItemMeta) -> Unit): ItemWrapper
    {
        return this.apply {
            val item = this.itemStack
            val meta = item.itemMeta

            if (meta != null)
            {
                edit(meta)
                item.itemMeta = meta
            }
        }
    }
}