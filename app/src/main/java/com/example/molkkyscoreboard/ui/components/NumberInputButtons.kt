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

@Composable
fun NumberInputButtons(
    team: Team,
    onNumberClick: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 0..3) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (j in 1..3) {
                    val number = i * 3 + j
                    if (number <= 12) {
                        Button(
                            onClick = { onNumberClick(team.id, number) },
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp)
                        ) {
                            Text(text = number.toString())
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun NumberInputButtonsPreview() {
    NumberInputButtons(
        team = Team(name = "Team A", members = listOf(Member(name = "Member A"))),
        onNumberClick = { team, number -> println("Number clicked: $number") }
    )
}
