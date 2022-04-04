package io.github.devrawr.practice.match.event.type

import io.github.devrawr.practice.match.Match
import io.github.devrawr.practice.match.event.MatchEvent
import io.github.devrawr.practice.match.team.MatchTeam
import java.util.*

class MatchPlayerDeathEvent(
    match: Match,
    val diedId: UUID,
    val diedTeam: MatchTeam
) : MatchEvent(match)