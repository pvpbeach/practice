package io.github.devrawr.practice.match.event.type

import io.github.devrawr.practice.match.Match
import io.github.devrawr.practice.match.event.MatchEvent

class MatchCreateEvent(match: Match) : MatchEvent(match)