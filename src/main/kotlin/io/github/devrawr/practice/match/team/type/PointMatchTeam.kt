package io.github.devrawr.practice.match.team.type

import io.github.devrawr.practice.match.team.MatchTeam
import java.util.*

class PointMatchTeam(ids: List<UUID>) : MatchTeam(ids)
{
    var points = 0
}