package pt.isel.pdm.gomokuroyale.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.game.play.domain.BoardWin
import pt.isel.pdm.gomokuroyale.game.play.ui.BoardView
import pt.isel.pdm.gomokuroyale.game.play.ui.GomokuViewModel

/**
 * Simple temporary function just for testing, keeps the content centered
 */
@Composable
fun MainScreen(vm: GomokuViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
        //AuthorInformationScreen()
        Spacer(modifier = Modifier.size(15.dp))
        Button(onClick = { vm.newGame("test") }) {
            Text("New Game")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    MainScreen()
//}