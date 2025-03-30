package com.example.molkkyscoreboard.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.molkkyscoreboard.R
import com.example.molkkyscoreboard.ui.theme.MolkkyScoreBoardTheme

@Composable
fun BoardScreen(
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
                painter = painterResource(R.drawable.ic_launcher_foreground),
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
                "次の Player は Member A です",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            // スコアボードの表を表示
            ScoreBoard()
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        ) {
            ResultButton(
                labelResourceId = R.string.result,
                onClick = { onNextButtonClicked(0) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun ScoreBoard() {
    Column {
        // ヘッダ行
        Row {
            Text(" ", modifier = Modifier.weight(1f))
            Text("7", modifier = Modifier.weight(1f))
            Text("8", modifier = Modifier.weight(1f))
            Text("9", modifier = Modifier.weight(1f))
        }
        // 各チームの行
        Row {
            Text("Team A", modifier = Modifier.weight(1f))
            Text("30", modifier = Modifier.weight(1f))
            Text("40", modifier = Modifier.weight(1f))
            Text("40", modifier = Modifier.weight(1f))
        }
        Row {
            Text("Team B", modifier = Modifier.weight(1f))
            Text("35", modifier = Modifier.weight(1f))
            Text("45", modifier = Modifier.weight(1f))
            Text("50", modifier = Modifier.weight(1f))
        }
        Row {
            Text("Team C", modifier = Modifier.weight(1f))
            Text("22", modifier = Modifier.weight(1f))
            Text("32", modifier = Modifier.weight(1f))
            Text("", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ResultButton(
    @StringRes labelResourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
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
            onNextButtonClicked = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
