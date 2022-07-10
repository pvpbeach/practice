package io.github.devrawr.practice.kit

import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.ConditionFailedException
import io.github.devrawr.inject.Inject
import io.github.devrawr.inject.inject
import io.github.devrawr.practice.commands.KitCommand
import io.github.devrawr.practice.kit.queue.Queue
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

val KIT_TEMPLATE = Kit("test").apply {
    this.icon = Material.DIAMOND_SWORD

    this
        .defaultLayout
        .contents[0] =
        ItemStack(
            Material.DIAMOND_SWORD
        )

    this.flags += KitFlag.Build
    this.flags += KitFlag.DespawnBlock
}

object KitService
{
    val kits = mutableListOf<Kit>(
        KIT_TEMPLATE
    )

    val queueCache = hashMapOf<UUID, Queue>()

    private val manager by Inject.inject<BukkitCommandManager>()

    fun registerCommands()
    {
        manager.commandContexts.registerContext(Kit::class.java) {
            val arg = it.popFirstArg()
            val kit = kits.firstOrNull { kit ->
                kit.id == arg
            } ?: throw ConditionFailedException("No kit found by name \"${arg}\"")

            return@registerContext kit
        }

        manager.registerCommand(KitCommand)
    }
}