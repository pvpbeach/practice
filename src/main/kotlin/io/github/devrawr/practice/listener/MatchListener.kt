package io.github.devrawr.practice.listener

import io.github.devrawr.events.Events
import io.github.devrawr.practice.kit.KitFlag
import io.github.devrawr.practice.kit.KitService
import io.github.devrawr.practice.match.MatchService
import io.github.devrawr.practice.match.MatchState
import io.github.devrawr.practice.match.MatchType
import io.github.devrawr.practice.match.event.type.MatchStartEvent
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack

object MatchListener : Listener
{
    init
    {
        this.registerBlockPlace()
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent)
    {
        val player = event.player
        val match = MatchService.matches[player.uniqueId]
            ?: return

        if (match.state == MatchState.Starting)
        {
            // instead of handing the yaw/pitch over to the new yaw/pitch, we could've just returned earlier
            // if the player didn't move their x/y/z at all. but this should work too, we'll change it if
            // this does not seem to work.
            // this should technically work better since comparing whether the player moved their x/y/z could
            // cause them to allow a tiny bit, while for NoDebuff etc this wouldn't matter, for gamemodes like
            // sumo or bridges, this would allow the player to get an unfair advantage.
            // comparing doubles COULD work, but it wouldn't be as accurate as this either, considering working with doubles can be innacurate.
            val yaw = event.to.yaw
            val pitch = event.to.pitch

            event.to = Location(
                event.to.world, event.from.x, event.from.y, event.from.z, yaw, pitch
            )
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent)
    {
        val player = event.player
        val inventory = player.inventory

        inventory.addItem(
            ItemStack(
                Material.IRON_SWORD
            )
        )

        val kit = KitService.kits.first()

        // add player to queue... temporary for testing!
        val queue = kit
            .retrieveQueueOfType(MatchType.Solo)

        if (queue.entries.firstOrNull {
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

    @EventHandler
    fun onStart(event: MatchStartEvent)
    {
        event.match.execute {
            it.sendMessage("${ChatColor.GREEN}Started!")
        }
    }

    private fun registerBlockPlace()
    {
        listOf(
            BlockPlaceEvent::class,
            BlockBreakEvent::class
        ).forEach { type ->
            Events
                .listenTo(type.java)
                .on {
                    val player = when (it)
                    {
                        is BlockBreakEvent -> it.player
                        is BlockPlaceEvent -> it.player
                        else -> null
                    }

                    if (player != null)
                    {
                        val match = MatchService.matches[player.uniqueId]
                            ?: return@on

                        if (match.kit.flags.contains(KitFlag.Build))
                        {
                            it.isCancelled = true
                            return@on
                        }

                        match.trackBlock(it.block)
                    }
                }
        }
    }
}