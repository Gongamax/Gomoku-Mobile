package pt.isel.pdm.gomokuroyale.matchHistory.domain

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Piece
import pt.isel.pdm.gomokuroyale.http.services.games.GameService
import pt.isel.pdm.gomokuroyale.util.onFailureResult
import pt.isel.pdm.gomokuroyale.util.onSuccessResult

class MatchHistoryViewModel(
    private val repository: UserInfoRepository,
    private val service: GameService): ViewModel() {

    companion object {
        fun factory(repository: UserInfoRepository, service: GameService) = viewModelFactory {
            MatchHistoryViewModel(repository, service)
        }
    }

    private val _state = MutableStateFlow<MatchHistoryScreenState>(Idle)
    val state: Flow<MatchHistoryScreenState> get() = _state.asStateFlow()

    fun fetchMatchHistory(id: Int) {
        check(_state.value is FetchedPlayerInfo) { "Cannot fetch match history while loading" }
        val userInfo = checkNotNull((_state.value as FetchedPlayerInfo).userInfo)
        _state.value = FetchingMatchHistory
        viewModelScope.launch {
            val result = kotlin.runCatching { service.getUserGames(userInfo.accessToken, id) }
            val res = result.getOrNull()
            if (res == null)
                _state.value = FailedToFetch(Exception("Failed to fetch match history"))
            else{
                res.onSuccessResult { matchesList ->
                    val matchInfoList = matchesList.map { match ->
                        val gameResult = if (match.winner?.id?.value == id) Result.Win else Result.Loss
                        val opponent = if (match.userWhite.id.value == id) match.userBlack else match.userWhite
                        val myPiece = if (match.userWhite.id.value == id) Piece.WHITE else Piece.BLACK
                        MatchInfo(gameResult, match.variant.name, opponent.username, myPiece)
                    }
                    _state.value = FetchedMatchHistory(matchInfoList)
                }.onFailureResult {
                    _state.value = FailedToFetch(Exception("Failed to fetch match history"))
                }
            }
        }
    }

    fun resetToIdle() {
        check(_state.value is FetchedMatchHistory) { "Cannot reset to fetching player info while loading" }
        _state.value = Idle
    }

    fun fetchPlayerInfo() {
        check(_state.value is Idle) { "Cannot fetch user info while loading" }

        _state.value = FetchingPlayerInfo
        viewModelScope.launch {
            val result = kotlin.runCatching { repository.getUserInfo() }
            val res = result.getOrNull()
            if (res == null)
                _state.value = FailedToFetch(Exception("Failed to fetch user info"))
            else
                _state.value = FetchedPlayerInfo(res)
        }
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