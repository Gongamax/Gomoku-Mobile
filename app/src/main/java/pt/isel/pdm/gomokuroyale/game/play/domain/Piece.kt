package pt.isel.pdm.gomokuroyale.game.play.domain

enum class Piece {
    BLACK, WHITE;
    fun other() = if (this == BLACK) WHITE else BLACK
}