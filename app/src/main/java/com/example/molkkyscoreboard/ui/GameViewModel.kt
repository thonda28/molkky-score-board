package com.example.molkkyscoreboard.ui

import androidx.lifecycle.ViewModel
import com.example.molkkyscoreboard.data.GameUiState
import com.example.molkkyscoreboard.data.Member
import com.example.molkkyscoreboard.data.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

const val MAX_CONSECUTIVE_FAILURE = 3
const val WINNING_SCORE = 50
const val RESET_SCORE = 25

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun resetUiState() {
        _uiState.value = GameUiState()
    }

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

    fun deleteTeam(teamId: String) {
        _uiState.update { currentState ->
            val updatedTeams = currentState.teams.filterNot { it.id == teamId }
            currentState.copy(teams = updatedTeams)
        }
    }

    fun addMemberToTeam(teamId: String, memberName: String) {
        val team = _uiState.value.teams.find { it.id == teamId } ?: return

        _uiState.update { currentState ->
            val updatedTeam = team.copy(
                members = team.members + Member(name = memberName)
            )
            val updatedTeams = currentState.teams.map {
                if (it.id == teamId) updatedTeam else it
            }
            currentState.copy(teams = updatedTeams)
        }
    }

    fun addScoreToTeam(teamId: String, score: Int) {
        if (score > 0) {
            succeed(teamId, score)
        } else {
            fail(teamId)
        }

        // 終了条件を満たしているか確認する
        // WINNING_SCORE のスコアを持つチームがいる
        val teams = _uiState.value.teams
        if (teams.any { it.scores.lastOrNull() == WINNING_SCORE }) {
            // 勝者が決まった場合、次のチームに移動しない
            setWinner(teamId)
            return
        }
        // 1チーム以外が失格している
        if (teams.count { it.isDisqualified } == teams.size - 1) {
            // 失格していないチームを勝者にする
            val winner = teams.first { !it.isDisqualified }
            setWinner(winner.id)
            return
        }

        // 最新のスコアに score を足したものを scores に追加する
        incrementNextPlayerIndex(teamId)
        incrementNextTeamIndex()
    }

    private fun succeed(teamId: String, score: Int) {
        val team = _uiState.value.teams.find { it.id == teamId } ?: return

        _uiState.update { currentState ->
            val lastScore = team.scores.lastOrNull() ?: 0
            val newScore = lastScore + score

            val updatedTeam = when {
                newScore > WINNING_SCORE -> {
                    team.copy(scores = team.scores + RESET_SCORE, consecutiveFailure = 0)
                }

                else -> {
                    team.copy(scores = team.scores + newScore, consecutiveFailure = 0)
                }
            }

            val updatedTeams = currentState.teams.map {
                if (it.id == teamId) updatedTeam else it
            }
            currentState.copy(teams = updatedTeams)
        }
    }

    private fun setWinner(teamId: String) {
        val team = _uiState.value.teams.find { it.id == teamId } ?: return

        _uiState.update { currentState ->
            currentState.copy(
                winner = team,
            )
        }
    }

    private fun fail(teamId: String) {
        val team = _uiState.value.teams.find { it.id == teamId } ?: return

        // 連続失敗回数をインクリメント
        _uiState.update { currentState ->
            val lastScore = team.scores.lastOrNull() ?: 0
            val updatedTeam = team.copy(
                scores = team.scores + lastScore,
                consecutiveFailure = team.consecutiveFailure + 1,
                isDisqualified = team.consecutiveFailure + 1 >= MAX_CONSECUTIVE_FAILURE,
            )

            val updatedTeams = currentState.teams.map {
                if (it.id == teamId) updatedTeam else it
            }
            currentState.copy(teams = updatedTeams)
        }
    }

    private fun incrementNextPlayerIndex(teamId: String) {
        val team = _uiState.value.teams.find { it.id == teamId } ?: return

        _uiState.update { currentState ->
            val updatedTeam = team.copy(
                nextPlayerIndex = (team.nextPlayerIndex + 1) % team.members.size
            )
            val updatedTeams = currentState.teams.map {
                if (it.id == teamId) updatedTeam else it
            }
            currentState.copy(teams = updatedTeams)
        }
    }

    private fun incrementNextTeamIndex() {
        _uiState.update { currentState ->
            var updatedNextTeamIndex = (currentState.nextTeamIndex + 1) % currentState.teams.size
            while (currentState.teams[updatedNextTeamIndex].isDisqualified) {
                updatedNextTeamIndex = (updatedNextTeamIndex + 1) % currentState.teams.size
            }
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
