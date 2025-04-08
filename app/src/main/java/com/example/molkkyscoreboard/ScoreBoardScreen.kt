package com.example.molkkyscoreboard

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.molkkyscoreboard.ui.BoardScreen
import com.example.molkkyscoreboard.ui.GameViewModel
import com.example.molkkyscoreboard.ui.MemberScreen
import com.example.molkkyscoreboard.ui.ResultScreen
import com.example.molkkyscoreboard.ui.StartScreen
import com.example.molkkyscoreboard.ui.TeamScreen

enum class ScoreBoardScreen(
    @StringRes val title: Int,
) {
    Start(title = R.string.app_name),
    Team(title = R.string.register_team),
    Member(title = R.string.register_member),
    Board(title = R.string.board),
    Result(title = R.string.result),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreBoardAppBar(
    currentScreen: ScoreBoardScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors =
            TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                    )
                }
            }
        },
    )
}

@Preview
@Composable
fun ScoreBoardApp(
    viewModel: GameViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen =
        ScoreBoardScreen.valueOf(
            backStackEntry?.destination?.route ?: ScoreBoardScreen.Start.name,
        )

    Scaffold(
        topBar = {
            ScoreBoardAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
            )
        },
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = ScoreBoardScreen.Start.name,
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding),
        ) {
            composable(route = ScoreBoardScreen.Start.name) {
                StartScreen(
                    onNextButtonClicked = {
                        viewModel.resetUiState()
                        navController.navigate(ScoreBoardScreen.Team.name)
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                dimensionResource(R.dimen.padding_medium),
                            ),
                )
            }
            composable(route = ScoreBoardScreen.Team.name) {
                TeamScreen(
                    teams = uiState.teams,
                    onTeamNameEntered = { teamName -> viewModel.addTeam(teamName) },
                    onNextButtonClicked = {
                        navController.navigate(ScoreBoardScreen.Member.name)
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                dimensionResource(R.dimen.padding_medium),
                            ),
                )
            }
            composable(route = ScoreBoardScreen.Member.name) {
                MemberScreen(
                    teams = uiState.teams,
                    onMemberNameEntered = { teamId, memberName ->
                        viewModel.addMemberToTeam(
                            teamId,
                            memberName,
                        )
                    },
                    onNextButtonClicked = {
                        navController.navigate(ScoreBoardScreen.Board.name)
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                dimensionResource(R.dimen.padding_medium),
                            ),
                )
            }
            composable(route = ScoreBoardScreen.Board.name) {
                BoardScreen(
                    uiState = uiState,
                    addScoreEntered = { team, score ->
                        viewModel.addScoreToTeam(
                            team,
                            score,
                        )
                    },
                    onNextButtonClicked = {
                        navController.navigate(ScoreBoardScreen.Result.name)
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                dimensionResource(R.dimen.padding_medium),
                            ),
                )
            }
            composable(route = ScoreBoardScreen.Result.name) {
                ResultScreen(
                    winner = uiState.winner,
                    onNextButtonClicked = {
                        navController.navigate(ScoreBoardScreen.Start.name) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                dimensionResource(R.dimen.padding_medium),
                            ),
                )
            }
        }
    }
}
