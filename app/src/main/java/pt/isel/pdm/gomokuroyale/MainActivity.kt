package pt.isel.pdm.gomokuroyale

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.model.BoardWin
import pt.isel.pdm.gomokuroyale.ui.BoardView
import pt.isel.pdm.gomokuroyale.ui.GomokuViewmodel
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm = remember { GomokuViewmodel() }
            GomokuRoyaleTheme {
                ScreenPrep(vm)
            }
        }
    }
}

/**
 * Simple temporary function just for testing, keeps the content centered
 */
@Composable
fun ScreenPrep(vm: GomokuViewmodel) {
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
