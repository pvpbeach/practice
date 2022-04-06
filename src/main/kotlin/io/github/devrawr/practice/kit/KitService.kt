package io.github.devrawr.practice.kit

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

val KIT_TEMPLATE = Kit("test").apply {
    this.icon = Material.DIAMOND_SWORD

    this.defaultLayout.contents[0] = ItemStack(
        Material.DIAMOND_SWORD
    )
}

object KitService
{
    val kits = mutableListOf<Kit>(
        KIT_TEMPLATE
    )
}