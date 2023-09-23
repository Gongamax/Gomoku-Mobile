package pt.isel.pdm.gomokuroyale.model

import android.util.Log

data class Game(
    val localPlayer: Player, //
    val board: Board,
    val forfeitedBy : Player? = null,
)

fun Game.makeMove(at: Cell): Game {
    if (board is BoardRun) {
        Log.v("Game", "$localPlayer and ${board.turn}")
        //(localPlayer == board.turn) { "Is not your turn" } //ignoring this check for now
    }
    return copy(board = board.play(at))
}

fun createGame(id : String) = Game(Player.BLACK, createBoard(Player.BLACK))