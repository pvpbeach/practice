package io.github.devrawr.practice.match.event.type

import io.github.devrawr.practice.match.Match
import io.github.devrawr.practice.match.event.MatchEvent
import io.github.devrawr.practice.match.team.MatchTeam

class MatchEndEvent(
    match: Match,
    val winner: MatchTeam,
    val loser: MatchTeam
) : MatchEvent(match)