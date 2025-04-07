package com.example.molkkyscoreboard.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.molkkyscoreboard.data.Member
import com.example.molkkyscoreboard.data.Team

const val MAX_NUMBER = 12
const val ROW_NUMBERS = 3
const val COL_NUMBERS = 4

@Composable
fun NumberInputButtons(
    team: Team,
    numberButtonEnabled: Boolean,
    onNumberClick: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 0 until ROW_NUMBERS) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (j in 0 until COL_NUMBERS) {
                    val number = i * COL_NUMBERS + j + 1
                    Button(
                        onClick = { onNumberClick(team.id, number) },
                        enabled = numberButtonEnabled,
                        modifier = Modifier
                            .width(75.dp)
                            .height(45.dp)
                    ) {
                        Text(text = number.toString())
                    }
                }
            }
        }
        Button(
            onClick = { onNumberClick(team.id, 0) },
            enabled = numberButtonEnabled,
            modifier = Modifier
                .width(150.dp)
                .height(45.dp)
        ) {
            Text(text = "Miss")
        }
    }
}

@Preview
@Composable
fun NumberInputButtonsPreview() {
    NumberInputButtons(
        team = Team(name = "Team A", members = listOf(Member(name = "Member A"))),
        numberButtonEnabled = true,
        onNumberClick = { team, number -> println("Number clicked: $number") }
    )
}
