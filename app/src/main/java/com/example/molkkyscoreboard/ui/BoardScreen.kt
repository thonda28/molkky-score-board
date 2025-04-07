package com.example.molkkyscoreboard.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.molkkyscoreboard.R
import com.example.molkkyscoreboard.data.GameUiState
import com.example.molkkyscoreboard.data.Member
import com.example.molkkyscoreboard.data.Team
import com.example.molkkyscoreboard.ui.components.NumberInputButtons
import com.example.molkkyscoreboard.ui.theme.MolkkyScoreBoardTheme

const val TABLE_COLUMN_COUNT = 3

@Composable
fun BoardScreen(
    uiState: GameUiState,
    addScoreEntered: (String, Int) -> Unit,
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        val currentTeam = uiState.teams[uiState.nextTeamIndex]
        val currentMember = currentTeam.members[currentTeam.nextPlayerIndex]

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Image(
                painter = painterResource(R.drawable.sports_molkky_morukku_man),
                contentDescription = null,
                modifier = Modifier.width(300.dp),
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Text(
                stringResource(R.string.board),
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Text(
                "次の Player は ${currentMember.name} です",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            // 得点を入力する
            NumberInputButtons(
                currentTeam,
                numberButtonEnabled = uiState.winner == null,
                onNumberClick = addScoreEntered,
                modifier = Modifier.widthIn(min = 250.dp)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            // スコアボードの表を表示
            ScoreBoard(uiState)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        ) {
            ResultButton(
                winner = uiState.winner,
                labelResourceId = R.string.result,
                onClick = { onNextButtonClicked(0) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun ScoreBoard(uiState: GameUiState) {
    Column {
        // ヘッダ行
        val currentAttempt = uiState.attempt
        Row {
            Text(" ", modifier = Modifier.weight(1f))
            if (currentAttempt < TABLE_COLUMN_COUNT) {
                for (i in 1..TABLE_COLUMN_COUNT) {
                    Text("$i", modifier = Modifier.weight(1f))
                }
            } else {
                for (i in 1..TABLE_COLUMN_COUNT) {
                    Text(
                        "${currentAttempt - TABLE_COLUMN_COUNT + i}",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        // 各チームの行
        uiState.teams.forEach { team ->
            Row {
                Text(team.name, modifier = Modifier.weight(1f))
                var rangeStart = 0
                var rangeEnd = TABLE_COLUMN_COUNT
                if (currentAttempt > TABLE_COLUMN_COUNT) {
                    rangeStart = currentAttempt - TABLE_COLUMN_COUNT
                    rangeEnd = currentAttempt
                }
                // スコアが少ない場合は、空白を表示する
                for (i in rangeStart until rangeEnd) {
                    Text(
                        if (team.scores.size > i) team.scores[i].toString() else "",
                        modifier = Modifier
                            .weight(1f)
                            .background(color = selectColor(team.consecutiveFailure))
                    )
                }
            }
        }
    }
}

private fun selectColor(consecutiveFailure: Int): Color {
    return when (consecutiveFailure) {
        0 -> Color.Transparent
        1 -> Color.Yellow
        2 -> Color.Red
        else -> Color.Gray
    }

}

@Composable
fun ResultButton(
    winner: Team?,
    @StringRes labelResourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = winner != null,
        modifier = modifier.widthIn(min = 250.dp),
    ) {
        Text(stringResource(labelResourceId))
    }
}

@Preview
@Composable
fun BoardPreview() {
    MolkkyScoreBoardTheme {
        BoardScreen(
            uiState = GameUiState(
                teams = listOf(
                    Team(
                        name = "Team A",
                        members = listOf(Member(name = "Player A")),
                        scores = listOf(5, 10, 18, 23, 29),
                    ),
                    Team(
                        name = "Team B",
                        members = listOf(Member(name = "Player B")),
                        scores = listOf(6, 10, 12, 18),
                    ),
                    Team(
                        name = "Team C",
                        members = listOf(Member(name = "Player C")),
                        scores = listOf(7, 8, 8, 20),
                    ),
                ),
                attempt = 5,
                nextTeamIndex = 1,
            ),
            addScoreEntered = { _, _ -> },
            onNextButtonClicked = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
