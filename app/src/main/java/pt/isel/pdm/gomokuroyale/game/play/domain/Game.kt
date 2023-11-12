package pt.isel.pdm.gomokuroyale.game.play.domain

import android.util.Log
import pt.isel.pdm.gomokuroyale.game.play.domain.Board.Companion.createBoard
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variants

data class Game(
    val localPlayer: Piece, //
    val board: Board,
    val forfeitedBy: Piece? = null,
)

//TODO: Change this Logic
fun Game.makeMove(at: Cell): Game {
    var turn: Piece? = null
    if (board is BoardRun) {
        Log.v("Game", "$localPlayer and ${board.turn}")
        //(localPlayer == board.turn) { "Is not your turn" } //ignoring this check for now
        turn = board.turn
    }
    if (turn == null)
        turn = localPlayer
    return copy(board = board.playRound(at, turn.other()))
}

fun createGame() = Game(Piece.BLACK, createBoard(Piece.BLACK, Variants.STANDARD))