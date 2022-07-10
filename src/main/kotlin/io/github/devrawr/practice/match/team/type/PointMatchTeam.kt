package io.github.devrawr.practice.match.team.type

import io.github.devrawr.practice.match.team.MatchTeam
import java.util.*


class PointMatchTeam
@Suppress("DEPRECATION")
@Deprecated(
    "Should use Kit#createTeamFromIds() for more accurate instantiation, as it creates match team of specific type",
    ReplaceWith(
        "kit.createTeamFromIds(ids)"
    )
)
constructor(ids: MutableMap<UUID, Boolean>) : MatchTeam(ids)
{
    var points = 0
}