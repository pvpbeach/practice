package io.github.devrawr.practice.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.ConditionFailedException
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Subcommand
import io.github.devrawr.practice.kit.Kit
import io.github.devrawr.practice.kit.KitService
import io.github.devrawr.practice.match.MatchService
import org.bukkit.ChatColor
import org.bukkit.entity.Player

@CommandAlias("kit|kits")
@CommandPermission("practice.kits")
object KitCommand : BaseCommand()
{
    @Default
    @HelpCommand
    fun help(help: CommandHelp)
    {
        help.showHelp()
    }

    @Subcommand("create")
    fun create(player: Player, name: String)
    {
        if (KitService
                .kits
                .any { it.id == name }
        )
        {
            throw ConditionFailedException("Kit with that name already exists.")
        }

        KitService
            .kits
            .add(
                Kit(id = name)
            )
        player.sendMessage("${ChatColor.GREEN}Success! Created kit with name ${name}")
    }

    @Subcommand("delete")
    fun delete(player: Player, kit: Kit)
    {
        if (MatchService
                .matches
                .any {
                    it.value.kit == kit
                }
        )
        {
            throw ConditionFailedException("There are players in a match with that kit.")
        }
        KitService
            .kits
            .remove(kit)


        player.sendMessage(
            "${ChatColor.RED}Deleted!"
        )
    }

    @Subcommand("copy|copyinv|setinv|inventory|inv|yourmom")
    fun copy(player: Player, kit: Kit)
    {
        kit
            .defaultLayout
            .armor = player.inventory.armorContents

        kit
            .defaultLayout
            .contents = player.inventory.contents

        player.sendMessage(
            "${ChatColor.GREEN}Success! Copied your inventory into ${kit.displayName}"
        )
    }
}