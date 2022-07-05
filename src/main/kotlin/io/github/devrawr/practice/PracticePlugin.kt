package io.github.devrawr.practice

import io.github.devrawr.inject.Injector
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
            }

        // register listeners
        Bukkit.getPluginManager().registerEvents(
            this,
            MatchListener, GeneralListener
        )
    }
}

fun PluginManager.registerEvents(plugin: JavaPlugin, vararg listeners: Listener)
{
    for (listener in listeners)
    {
        this.registerEvents(listener, plugin)
    }
}