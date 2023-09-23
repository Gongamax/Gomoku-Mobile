package pt.isel.pdm.gomokuroyale.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.isel.pdm.gomokuroyale.model.Cell
import pt.isel.pdm.gomokuroyale.model.Game
import pt.isel.pdm.gomokuroyale.model.createGame
import pt.isel.pdm.gomokuroyale.model.makeMove
import java.lang.Exception

class GomokuViewmodel(

) {
    var game by mutableStateOf<Game?>(null)
        private set

    fun newGame(name: String) {
        try {
            Log.v("Viewmodel","Creating New Game")
            val g = game
            if (g == null || g.board.moves.isNotEmpty())
                game = createGame(name)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun makeMove(cell: Cell) =
        try {
            Log.v("Viewmodel","Inside makeMove of class Viewmodel")
            Log.v("Viewmodel", "The game is $game")
            game = game?.makeMove(cell)
        } catch (e: Exception) {
            e.message?.let { Log.v("Viewmodel", it) }
        }

}