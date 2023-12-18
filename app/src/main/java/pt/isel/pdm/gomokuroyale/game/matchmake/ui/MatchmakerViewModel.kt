package pt.isel.pdm.gomokuroyale.game.matchmake.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.TAG
import pt.isel.pdm.gomokuroyale.game.lobby.domain.MatchInfo
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingInputModel
import pt.isel.pdm.gomokuroyale.http.domain.MatchmakingStatus
import pt.isel.pdm.gomokuroyale.util.IOState
import pt.isel.pdm.gomokuroyale.util.idle
import pt.isel.pdm.gomokuroyale.util.loadFailure
import pt.isel.pdm.gomokuroyale.util.loadSuccess
import pt.isel.pdm.gomokuroyale.util.loading

class MatchmakerViewModel(
    private val service: GomokuService,
    private val matchInfo: MatchInfo?
) : ViewModel() {

    private val _status: MutableStateFlow<IOState<MatchmakingStatus>> = MutableStateFlow(idle())
    val status: StateFlow<IOState<MatchmakingStatus>> = _status.asStateFlow()

    private val gameService = service.gameService

    fun findGame() {
        viewModelScope.launch {
//            if (matchInfo == null) {
//                _status.value = loadFailure(Exception("MatchInfo is null"))
//                return@launch
//            }
//
//            _status.value = loading()
//            Log.v(TAG, "Finding game with variant ${matchInfo.variant}")
//            Log.v(TAG, "Finding game with token ${matchInfo.userInfo.accessToken}")
//
//            val response = gameService.matchmaking(
//                token = matchInfo.userInfo.accessToken,
//                GameMatchmakingInputModel(variant = matchInfo.variant.toString())
//            )
//
//            while (true) {
//                val matchEntry = gameService.getMatchmakingStatus(
//                    matchInfo.userInfo.accessToken,
//                    response.properties.id
//                )
//                Log.v("MatchmakerViewModel", "Matchmaking status: $matchEntry")
//                val matchStatus = MatchmakingStatus.valueOf(matchEntry.properties.state)
//                _status.value = loadSuccess(matchStatus)
//                if (matchStatus == MatchmakingStatus.MATCHED) {
//                    //TODO: navigate to game
//                    break
//                } else {
//                    delay(POOLING_DELAY)
//                }
//            }
        }
    }

    fun leaveQueue() {
        viewModelScope.launch {
            if (matchInfo == null) {
                _status.value = loadFailure(Exception("MatchInfo is null"))
                return@launch
            }
            val HARDCODED_ID = 1 //TODO: REMOVE HARDCODED ID
            val response =
                service.gameService.cancelMatchmaking(matchInfo.userInfo.accessToken, HARDCODED_ID)
            _status.value = loadSuccess(MatchmakingStatus.LEFT_QUEUE)
        }
    }

    companion object {
        private const val POOLING_DELAY = 5000L
        fun factory(service: GomokuService, matchInfo: MatchInfo?) =
            viewModelFactory {
                initializer { MatchmakerViewModel(service, matchInfo) }
            }
    }
}