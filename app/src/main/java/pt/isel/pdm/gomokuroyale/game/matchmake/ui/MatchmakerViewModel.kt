package pt.isel.pdm.gomokuroyale.game.matchmake.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.game.lobby.domain.MatchInfo
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.dto.GameMatchmakingInputModel
import pt.isel.pdm.gomokuroyale.http.dto.GameMatchmakingStatusOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.util.MatchmakingStatus
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

    private

    fun findGame() {
        viewModelScope.launch {
            if (matchInfo == null) {
                _status.value = loadFailure(Exception("MatchInfo is null"))
                return@launch
            }

            _status.value = loading()

            val response = service.gameService.matchmaking(
                token = matchInfo.userInfo.accessToken,
                GameMatchmakingInputModel(variant = matchInfo.variant.toString())
            )

            if (response.isFailure)
                _status.value =
                    loadFailure(response.exceptionOrNull() ?: Exception("Unknown error"))

            while (true) {

                val matchStatus =
                    service.gameService.getMatchmakingStatus(matchInfo.userInfo.accessToken)
                when {
                    matchStatus.isSuccess -> {
                        val body = matchStatus.getOrNull() as GameMatchmakingStatusOutputModel
                        _status.value = loadSuccess(body.status)
                        if (body.status == MatchmakingStatus.MATCHED) {
                            //TODO: navigate to game
                            break
                        } else {
                            delay(POOLING_DELAY)
                        }
                    }

                    else -> {
                        _status.value =
                            loadFailure(response.exceptionOrNull() ?: Exception("Unknown error"))
                        break
                    }
                }
            }
        }
    }

    fun leaveQueue() {
        viewModelScope.launch {
            if (matchInfo == null) {
                _status.value = loadFailure(Exception("MatchInfo is null"))
                return@launch
            }
            val response = service.gameService.cancelMatchmaking(matchInfo.userInfo.accessToken)
            if (response.isFailure) {
                _status.value =
                    loadFailure(response.exceptionOrNull() ?: Exception("Unknown error"))
            } else {
                _status.value = loadSuccess(MatchmakingStatus.LEFT_QUEUE)
            }
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