package io.github.devrawr.practice.listener

import io.github.devrawr.events.Events
import io.github.devrawr.practice.extensions.player
import io.github.devrawr.practice.extensions.retrieveProfile
import io.github.devrawr.practice.kit.KitFlag
import io.github.devrawr.practice.match.MatchService
import io.github.devrawr.practice.match.MatchState
import io.github.devrawr.practice.match.event.type.MatchEndEvent
import io.github.devrawr.practice.match.event.type.MatchStartEvent
import io.github.devrawr.tasks.Tasks
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent

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
    fun onStart(event: MatchStartEvent)
    {
        event.match.execute {
            it.sendMessage("${ChatColor.GREEN}Started!")
        }
    }

    @EventHandler
    fun onEnd(event: MatchEndEvent)
    {
        event.match.execute { team ->
            listOf(
                "&7&m${"-".repeat(32)}",
                "&6Post-Match Inventories &7(click name to view)",
                "&aWinner: &e${
                    event.winner
                        .ids
                        .keys
                        .joinToString(", ") { it.player?.name ?: "" }
                }",
                "&cLoser: &e${
                    event.loser
                        .ids
                        .keys
                        .joinToString(", ") { it.player?.name ?: "" }
                }",
                "&7&m${"-".repeat(32)}",
            ).forEach {
                team.sendMessage(it)
            }
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent)
    {
        Tasks
            .sync()
            .delay(1L) {
                event.entity
                    .spigot()
                    .respawn()
            }
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent)
    {
        val player = event.player
        val match = MatchService.matches[player.uniqueId]

        match?.death(player.uniqueId)
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent)
    {
        event.isCancelled = true

        val entity = event.entity

        if (MatchService.matches[entity.uniqueId] == null)
        {
            return
        }

        event.isCancelled = false
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent)
    {
        val player = event.player
        val profile = player.uniqueId.retrieveProfile(false)

        val queue = profile.queue
        val match = profile.match

        if (queue != null)
        {
            queue
                .entries
                .forEach {
                    it.ids.remove(player.uniqueId)
                }

            queue
                .entries
                .removeIf {
                    it.ids.isEmpty()
                }
        }

        if (match != null)
        {
            match.death(player.uniqueId)
            match.sendMessage("${ChatColor.RED}${player.name} ${ChatColor.GOLD}has disconnected.")
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

                        if (match == null)
                        {
                            it.isCancelled = true
                            return@on
                        }

                        val bedTeam = match.getBedTeam(it.block.location)

                        if (bedTeam != null)
                        {
                            bedTeam.destroyedBed = true
                        }

                        if (!match.kit.flags.contains(KitFlag.Build))
                        {
                            it.isCancelled = true
                            return@on
                        }

                        if (it is BlockBreakEvent
                            && !match.isTrackedBlock(it.block.location) && !match.kit.flags.contains(KitFlag.BreakAll)
                        )
                        {
                            it.isCancelled =
                                true // this is not a tracked block. don't let the players break non-tracked blocks.

                            return@on
                        }

                        match.trackBlock(it.block)
                    }
                }
        }
    }
}