package pt.isel.pdm.gomokuroyale.matchHistory.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.viewModelFactory
import pt.isel.pdm.gomokuroyale.game.play.domain.Piece
import pt.isel.pdm.gomokuroyale.http.GameService
import pt.isel.pdm.gomokuroyale.util.IOState
import pt.isel.pdm.gomokuroyale.util.Idle

class MatchHistoryViewModel(private val service: GameService) {

    companion object {
        fun factory(service: GameService) = viewModelFactory {
            MatchHistoryViewModel(service)
        }
    }

    var matchInfo = mutableStateOf<IOState<List<MatchInfo>>>(Idle)

    fun getMatchHistory(page: Int) {
        TODO()
    }

}

data class InfoParam (val name : String, val value : String)

data class MatchInfo(
    val result: Result,
    val variant: String,
    val opponent: String,
    val myPiece: Piece
)

enum class Result {
    Win,
    Loss,
}

fun Piece.toColor() = if(this == Piece.BLACK) Color.Black else Color.White

fun Color.toOther()= if(this == Color.Black) Color.White else Color.Black