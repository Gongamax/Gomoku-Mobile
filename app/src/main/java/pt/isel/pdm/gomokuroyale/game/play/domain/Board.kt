package pt.isel.pdm.gomokuroyale.game.play.domain

import android.util.Log

const val BOARD_DIM = 15
const val WIN_LENGTH = 5
const val MAX_MOVES = BOARD_DIM * BOARD_DIM

typealias Moves = Map<Cell, Player>

/**
 * Represents a board of the game.
 * @property moves the map of the moves of the game.
 * @constructor Creates a board with the given [moves] that is map from [Cell] to [Player] ([Moves]).
 * There are four possible states of board: [BoardRun], [BoardWin] and [BoardDraw]
 * These hierarchies are to be used by pattern matching.
 */
sealed class Board(val moves: Moves) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false
        if (moves.size != (other as Board).moves.size) return false
        return when (this) {
            is BoardRun -> turn == (other as BoardRun).turn
            is BoardWin -> winner == (other as BoardWin).winner
            else -> true
        }
    }

    override fun hashCode(): Int = moves.hashCode()
}

open class BoardRun(moves: Moves, val turn: Player) : Board(moves)
class BoardWin(moves: Moves, val winner: Player) : Board(moves)
class BoardDraw(moves: Moves) : Board(moves)

fun createBoard(first: Player) = BoardRun(emptyMap(), first)

/**
 * Makes a move in [cell] cell by the current turn.
 * @throws IllegalArgumentException if the [cell] is already used.
 * @throws IllegalStateException if the game is over (Draw or Win).
 */
fun Board.play(cell: Cell): Board {
    return when (this) {
        is BoardRun -> {
            require(moves[cell] == null) { "Position $cell used" }
            val moves = moves + (cell to turn)
            Log.v("Model", "Its the turn of ${turn.name}")
            when {
                isWin(cell) -> BoardWin(moves, winner = turn)
                isDraw() -> BoardDraw(moves)
                else -> BoardRun(moves, turn.other())
            }
        }

        is BoardWin, is BoardDraw -> error("Game over")
    }
}

/**
 * Checks if the move in [cell] position is a winning move.
 */
private fun BoardRun.isWin(cell: Cell) =
    moves.size >= WIN_LENGTH * 2 - 2 &&
            (moves.filter { it.value == turn }.keys + cell).run {
                count { it.row == cell.row } == WIN_LENGTH ||
                        isDiagonalWin(cell)
            }

/**
 * Checks if the state of the board will end the game as a Draw.
 */
private fun BoardRun.isDraw() = moves.size == MAX_MOVES

//Auxiliary functions for isWin

private val diagonals = listOf<Direction>(
    Direction.DOWN_LEFT,
    Direction.DOWN_RIGHT,
    Direction.UP_LEFT,
    Direction.UP_RIGHT
)

private fun BoardRun.isDiagonalWin(cell: Cell): Boolean {
    val backSlash = mutableListOf(cell)
    val slash = mutableListOf(cell)
    val addToDiagonals = { dir: Direction, cells: List<Cell> ->
        when (dir) {
            Direction.DOWN_LEFT, Direction.UP_LEFT -> backSlash += cells
            Direction.DOWN_RIGHT, Direction.UP_RIGHT -> slash += cells
            else -> {}
        }
    }
    diagonals.forEach { dir ->
        val cells = cellsInDirection(cell, dir).takeWhile { it in moves && moves[it] == turn }
        addToDiagonals(dir, cells)
        if (backSlash.size >= WIN_LENGTH || slash.size >= WIN_LENGTH)
            return true
    }
    return false
}

