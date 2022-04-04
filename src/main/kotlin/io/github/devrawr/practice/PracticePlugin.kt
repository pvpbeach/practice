package io.github.devrawr.practice

import io.github.devrawr.inject.Injector
import org.bukkit.plugin.java.JavaPlugin

class PracticePlugin : JavaPlugin()
{
    override fun onEnable()
    {
        val injector = Injector.create<PracticePlugin>()

        injector.bind<JavaPlugin>() to this
    }
}