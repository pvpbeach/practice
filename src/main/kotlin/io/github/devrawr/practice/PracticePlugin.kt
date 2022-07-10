package io.github.devrawr.practice

import co.aikar.commands.BukkitCommandManager
import io.github.devrawr.inject.Injector
import io.github.devrawr.practice.kit.KitService
import io.github.devrawr.practice.listener.GeneralListener
import io.github.devrawr.practice.listener.MatchListener
import io.github.nosequel.data.DataHandler
import io.github.nosequel.data.connection.mongo.NoAuthMongoConnectionPool
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class PracticePlugin : JavaPlugin()
{
    companion object
    {
        lateinit var INSTANCE: PracticePlugin
    }

    override fun onEnable()
    {
        INSTANCE = this
        val manager = BukkitCommandManager(this).apply {
            this.enableUnstableAPI("help")
        }

        DataHandler
            .withConnectionPool<NoAuthMongoConnectionPool> {
                this.hostname = "127.0.0.1"
                this.port = 27017
                this.databaseName = "practice"
            }

        Injector
            .create<PracticePlugin>()
            .also {
                it.bind<JavaPlugin>() to this
                it.bind<Injector>() to it
                it.bind<BukkitCommandManager>() to manager
            }

        // register listeners
        Bukkit.getPluginManager().registerEvents(
            this,
            MatchListener, GeneralListener
        )

        KitService.registerCommands()
    }
}

fun PluginManager.registerEvents(plugin: JavaPlugin, vararg listeners: Listener)
{
    for (listener in listeners)
    {
        this.registerEvents(listener, plugin)
    }
}