package pt.isel.pdm.gomokuroyale.game.lobby.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FailedToFetch
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.LobbyScreenState
import pt.isel.pdm.gomokuroyale.game.lobby.domain.MatchInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import java.lang.IllegalStateException

class LobbyScreenViewModel(
    private val repository: UserInfoRepository
) : ViewModel() {

    companion object {
        fun factory(userInfoRepository: UserInfoRepository) = viewModelFactory {
            initializer { LobbyScreenViewModel(userInfoRepository) }
        }
    }

    private val _state = MutableStateFlow<LobbyScreenState>(FetchingPlayerInfo)

    val state: Flow<LobbyScreenState> get() = _state.asStateFlow()


    fun fetchPlayerInfo() {
        if (_state.value !is FetchingPlayerInfo)
            throw IllegalStateException("Cannot fetch user info while loading")

        _state.value = FetchingPlayerInfo
        viewModelScope.launch {
            val result = kotlin.runCatching { repository.getUserInfo() }
            if (result.getOrNull() == null)
                _state.value = FailedToFetch(result.exceptionOrNull() ?: Exception())
            else
                _state.value = FetchedPlayerInfo(result.getOrNull())
        }
    }

    fun fetchMatchInfo(variant: Variant) {
        val value =  _state.value
        if (value !is FetchedPlayerInfo)
            throw IllegalStateException("Cannot fetch match info while loading")

        _state.value = FetchingMatchInfo(checkNotNull(value.userInfo))
        viewModelScope.launch {
            val result = kotlin.runCatching { repository.getUserInfo() }
            val userInfo = checkNotNull(result.getOrNull())
            _state.value = FetchedMatchInfo(MatchInfo(userInfo, variant))
        }
    }

    fun resetToFetchingPlayerInfo() {
        if (_state.value !is FetchedMatchInfo)
            throw IllegalStateException("Cannot reset to fetching player info while loading")

        _state.value = FetchingPlayerInfo
    }
}