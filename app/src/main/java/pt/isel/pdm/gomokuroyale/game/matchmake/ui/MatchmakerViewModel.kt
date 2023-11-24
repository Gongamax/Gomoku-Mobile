package pt.isel.pdm.gomokuroyale.game.matchmake.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.dto.GameMatchmakingInputModel
import pt.isel.pdm.gomokuroyale.http.dto.GameMatchmakingStatusOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.util.MatchmakingStatus
import pt.isel.pdm.gomokuroyale.http.utils.Success
import pt.isel.pdm.gomokuroyale.util.LoadState
import pt.isel.pdm.gomokuroyale.util.failure
import pt.isel.pdm.gomokuroyale.util.idle
import pt.isel.pdm.gomokuroyale.util.loading
import pt.isel.pdm.gomokuroyale.util.success


//TODO: Implement this
class MatchmakerViewModel(
    private val service: GomokuService,
    private val playerInfo: MatchmakerActivity.PlayerInfo?
) : ViewModel() {

    var status by mutableStateOf<LoadState<MatchmakingStatus>>(idle())
        private set

    fun findGame() {
        viewModelScope.launch {
            status = loading()
            if (playerInfo == null) {
                status = failure(Exception("User not logged in"))
                return@launch
            }
            while (true) {
                val response = service.gameService.matchmaking(
                    token = playerInfo.token,
                    GameMatchmakingInputModel(
                        variant = ""
                    )
                )
                val matchStatus = service.gameService.getMatchmakingStatus(playerInfo.token)
//                when (matchStatus) {
//                    is Success<GameMatchmakingStatusOutputModel> -> {
//                        when (matchStatus.value.status) {
//                            MatchmakingStatus.PENDING -> {
//                                delay(POOLING_DELAY)
//                            }
//                            MatchmakingStatus.MATCHED -> {
//                                status = success(MatchmakingStatus.MATCHED)
//                                break
//                            }
//
//                        }
//                    }
//                }
            }

        }
    }
    companion object {
        private const val POOLING_DELAY = 5000L
        fun factory(service: GomokuService, playerInfo: MatchmakerActivity.PlayerInfo) =
            viewModelFactory {
                initializer { MatchmakerViewModel(service, playerInfo) }
            }
    }

}