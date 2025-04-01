package com.example.molkkyscoreboard.data

import java.util.UUID


data class Member(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
)

data class Team(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val members: List<Member>,
    val nextPlayerIndex: Int = 0,
    val scores: List<Int> = emptyList<Int>(),
)

data class GameUiState(
    val teams: List<Team> = emptyList(),
    val attempt: Int = 1,
    val nextTeamIndex: Int = 0,
    val winner: Team? = null,
)
