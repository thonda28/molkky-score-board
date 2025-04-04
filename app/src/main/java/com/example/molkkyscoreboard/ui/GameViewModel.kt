package com.example.molkkyscoreboard.ui

import androidx.lifecycle.ViewModel
import com.example.molkkyscoreboard.data.GameUiState
import com.example.molkkyscoreboard.data.Member
import com.example.molkkyscoreboard.data.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

const val WINNING_SCORE = 50
const val RESET_SCORE = 25

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

    fun addMemberToTeam(teamId: String, memberName: String) {
        val team = _uiState.value.teams.find { it.id == teamId } ?: return

        _uiState.update { currentState ->
            val updatedTeam = team.copy(
                members = team.members + Member(name = memberName)
            )
            val updatedTeams = currentState.teams.map {
                if (it.name == team.name) updatedTeam else it
            }
            currentState.copy(teams = updatedTeams)
        }
    }


    fun addScoreToTeam(teamId: String, score: Int) {
        val team = _uiState.value.teams.find { it.id == teamId } ?: return
        // TODO: 失格のケースを実装

        // 最新のスコアに score を足したものを scores に追加する
        _uiState.update { currentState ->
            val lastScore = team.scores.lastOrNull() ?: 0
            val newScore = lastScore + score

            val updatedTeam = when {
                newScore == WINNING_SCORE -> {
                    setWinner(team)
                    team.copy(scores = team.scores + newScore)
                }

                newScore > WINNING_SCORE -> {
                    team.copy(scores = team.scores + RESET_SCORE)
                }

                else -> {
                    team.copy(scores = team.scores + newScore)
                }
            }
            val updatedTeams = currentState.teams.map {
                if (it.name == team.name) updatedTeam else it
            }
            currentState.copy(teams = updatedTeams)
        }

        incrementNextPlayerIndex(teamId)
        incrementNextTeamIndex()
    }

    private fun setWinner(teamId: Team) {
        val team = _uiState.value.teams.find { it.id == teamId.id } ?: return

        _uiState.update { currentState ->
            currentState.copy(
                winner = team,
            )
        }
    }

    private fun incrementNextPlayerIndex(teamId: String) {
        val team = _uiState.value.teams.find { it.id == teamId } ?: return

        _uiState.update { currentState ->
            val updatedTeam = team.copy(
                nextPlayerIndex = (team.nextPlayerIndex + 1) % team.members.size
            )
            val updatedTeams = currentState.teams.map {
                if (it.name == team.name) updatedTeam else it
            }
            currentState.copy(teams = updatedTeams)
        }
    }

    private fun incrementNextTeamIndex() {
        _uiState.update { currentState ->
            val updatedNextTeamIndex = (currentState.nextTeamIndex + 1) % currentState.teams.size
            currentState.copy(nextTeamIndex = updatedNextTeamIndex)
        }

        // 更新後のチームインデックスが0なら incrementAttempt() を呼び出す
        if (_uiState.value.nextTeamIndex == 0) {
            incrementAttempt()
        }
    }

    private fun incrementAttempt() {
        _uiState.value = _uiState.value.copy(attempt = _uiState.value.attempt + 1)
    }
}
