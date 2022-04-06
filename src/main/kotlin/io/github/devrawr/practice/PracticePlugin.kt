package io.github.devrawr.practice

import io.github.devrawr.inject.Injector
import io.github.devrawr.practice.listener.MatchListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class PracticePlugin : JavaPlugin()
{
    override fun onEnable()
    {
        val injector = Injector.create<PracticePlugin>()

        injector.bind<JavaPlugin>() to this

        // register listeners
        Bukkit.getPluginManager().registerEvents(MatchListener, this)
    }
}