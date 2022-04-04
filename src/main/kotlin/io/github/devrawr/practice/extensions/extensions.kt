package io.github.devrawr.practice.extensions

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

val UUID.player: Player?
    get()
    {
        return Bukkit.getPlayer(this)
    }