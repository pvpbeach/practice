package io.github.devrawr.practice.match.tracking

import org.bukkit.Location

class TrackedBlock(
    val location: Location,
    val type: TrackedBlockType,
    val time: Long = System.currentTimeMillis()
)

enum class TrackedBlockType
{
    Place,
    Break
}