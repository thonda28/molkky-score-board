package com.example.molkkyscoreboard.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.molkkyscoreboard.R
import com.example.molkkyscoreboard.data.Member
import com.example.molkkyscoreboard.data.Team
import com.example.molkkyscoreboard.ui.theme.MolkkyScoreBoardTheme

const val MIN_MEMBER_COUNT = 1
const val MAX_MEMBER_COUNT = 6

@Composable
fun MemberScreen(
    teams: List<Team>,
    onMemberNameEntered: (String, String) -> Unit,
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
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
                stringResource(R.string.register_member),
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            teams.forEach { team ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(team.name, style = MaterialTheme.typography.bodyLarge)
                    team.members.forEach { member ->
                        Text(member.name, style = MaterialTheme.typography.bodyMedium)
                    }
                    MemberForm(
                        team = team,
                        labelResourceId = R.string.register_member,
                        onMemberNameEntered = onMemberNameEntered,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        ) {
            RegisterMemberButton(
                teams = teams,
                labelResourceId = R.string.register_member,
                onClick = { onNextButtonClicked(0) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun MemberForm(
    team: Team,
    @StringRes labelResourceId: Int,
    onMemberNameEntered: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var memberName by remember { mutableStateOf("") }

    if (!addMemberEnabled(team)) {
        return
    }

    OutlinedTextField(
        value = memberName,
        onValueChange = { memberName = it },
        label = { Text(stringResource(R.string.member_name)) },
        modifier = modifier.widthIn(min = 250.dp),
    )

    Button(
        onClick = {
            onMemberNameEntered(team.id, memberName)
            memberName = ""
        },
        modifier = modifier.widthIn(min = 250.dp),
    ) {
        Text(stringResource(labelResourceId))
    }
}

private fun addMemberEnabled(team: Team): Boolean {
    return team.members.size < MAX_MEMBER_COUNT
}

@Composable
fun RegisterMemberButton(
    teams: List<Team>,
    @StringRes labelResourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = registerMemberButtonEnabled(teams),
        modifier = modifier.widthIn(min = 250.dp),
    ) {
        Text(stringResource(labelResourceId))
    }
}

private fun registerMemberButtonEnabled(teams: List<Team>): Boolean {
    return teams.all { team ->
        team.members.size in MIN_MEMBER_COUNT..MAX_MEMBER_COUNT
    }
}

@Preview
@Composable
fun MemberPreview() {
    MolkkyScoreBoardTheme {
        MemberScreen(
            teams = listOf(
                Team(name = "Team A", members = listOf(Member(name = "Player A"))),
                Team(name = "Team B", members = listOf(Member(name = "Player B"))),
                Team(name = "Team C", members = listOf(Member(name = "Player C"))),
            ),
            onMemberNameEntered = { _, _ -> },
            onNextButtonClicked = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
