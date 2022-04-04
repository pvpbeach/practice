package io.github.devrawr.practice.kit

import org.bukkit.Material

class Kit(val id: String)
{
    var icon = Material.DIAMOND_SWORD
    var displayName = "${id}-display"
    var defaultLayout: KitLayout = KitLayout("Default")
    var flags = mutableListOf<KitFlag>()
}

enum class KitFlag
{
    Build,
}