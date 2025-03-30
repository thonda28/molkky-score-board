package com.example.molkkyscoreboard.data

data class Member(
    val name: String,
)

data class Team(
    val name: String,
    val members: List<Member>,
)

data class GameUiState(
    val teams: List<Team> = emptyList(),
    val attempt: Int = 0,
    val scores: List<Int> = emptyList(),
    val winner: Team = Team(name = "", members = emptyList())
)
