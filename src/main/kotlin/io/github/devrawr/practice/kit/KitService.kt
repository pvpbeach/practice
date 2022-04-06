package io.github.devrawr.practice.kit

import io.github.devrawr.practice.kit.queue.Queue
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

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

    val queueCache = hashMapOf<UUID, Queue>()
}