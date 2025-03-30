package com.example.molkkyscoreboard.ui

import androidx.lifecycle.ViewModel
import com.example.molkkyscoreboard.data.GameUiState
import com.example.molkkyscoreboard.data.Member
import com.example.molkkyscoreboard.data.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun addTeam(teamName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                teams = currentState.teams + Team(
                    name = teamName,
                    members = emptyList()
                )
            )
        }
    }

    fun addMemberToTeam(teamName: String, memberName: String) {
        _uiState.update { currentState ->
            val team = currentState.teams.find { it.name == teamName }
            requireNotNull(team) {
                "Team not found: $teamName"
            }

            val updatedTeam = team.copy(
                members = team.members + Member(memberName)
            )
            val updatedTeams = currentState.teams.map {
                if (it.name == teamName) updatedTeam else it
            }
            currentState.copy(teams = updatedTeams)
        }
    }
}
