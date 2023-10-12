package pt.isel.pdm.gomokuroyale.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.game.play.domain.BoardWin
import pt.isel.pdm.gomokuroyale.game.play.ui.BoardView
import pt.isel.pdm.gomokuroyale.game.play.ui.GomokuViewModel
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.about.AboutActivity
import pt.isel.pdm.gomokuroyale.ui.about.AboutScreen


const val MainScreenTestTag = "MainScreenTestTag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(vm: GomokuViewModel, onInfoRequested: () -> Unit = {}) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(MainScreenTestTag),
        topBar = {
            TopBar(
                navigation = NavigationHandlers(
                    onInfoRequested = onInfoRequested
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val board = vm.game?.board
            if (board is BoardWin)
                Text(
                    text = "The winner is player ${board.winner}",
                    style = MaterialTheme.typography.titleLarge
                )
            else
                BoardView(board, onClick = vm::makeMove)
            Spacer(modifier = Modifier.size(15.dp))
            Button(onClick = { vm.newGame("test") }) {
                Text("New Game")
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    MainScreen()
//}