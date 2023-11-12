package pt.isel.pdm.gomokuroyale.game.play.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.game.play.domain.Cell
import pt.isel.pdm.gomokuroyale.game.play.domain.Game
import pt.isel.pdm.gomokuroyale.game.play.domain.createGame
import pt.isel.pdm.gomokuroyale.game.play.domain.makeMove
import java.lang.Exception

class GomokuViewModel() : ViewModel() {
    var game : Game? by mutableStateOf(null)
        private set

    fun newGame() {
        try {
            Log.v("Viewmodel", "Creating New Game")
            val g = game
            if (g == null || g.board.moves.isNotEmpty())
                game = createGame()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun makeMove(cell: Cell) =
        try {
            Log.v("Viewmodel", "Inside makeMove of class Viewmodel")
            viewModelScope.launch {
                game = game?.makeMove(cell)
            }
        } catch (e: Exception) {
            e.message?.let { Log.v("Viewmodel", it) }
        }
}