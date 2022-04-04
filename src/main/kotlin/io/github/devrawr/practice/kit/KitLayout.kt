package io.github.devrawr.practice.kit

import io.github.devrawr.practice.extensions.player
import io.github.devrawr.practice.match.team.MatchTeam
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.logging.Level

class KitLayout(val name: String)
{
    var contents = arrayOfNulls<ItemStack>(27)
    var armor = arrayOfNulls<ItemStack>(4)

    fun equip(team: MatchTeam)
    {
        team.ids.forEach {
            equip(it)
        }
    }

    fun equip(id: UUID)
    {
        val player = id.player

        if (player != null)
        {
            val inventory = player.inventory

            inventory.clear()

            inventory.armorContents = this.armor
            inventory.contents = this.contents

            player.sendMessage("${ChatColor.YELLOW}You have equipped the ${this.name} kit.")
        } else
        {
            Bukkit.getLogger().log(Level.WARNING, "Could not apply kit to $id, no player found.")
        }
    }
}