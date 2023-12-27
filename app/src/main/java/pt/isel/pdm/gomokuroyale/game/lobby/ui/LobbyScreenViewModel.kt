package pt.isel.pdm.gomokuroyale.game.lobby.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.TAG
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FailedToFetch
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FailedToFetchVariants
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchVariants
import pt.isel.pdm.gomokuroyale.game.lobby.domain.LobbyScreenState
import pt.isel.pdm.gomokuroyale.game.lobby.domain.MatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.VariantRepository
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variants
import java.lang.IllegalStateException

class LobbyScreenViewModel(
    private val repository: UserInfoRepository,
    private val repositoryVariants: VariantRepository
) : ViewModel() {

    companion object {
        fun factory(userInfoRepository: UserInfoRepository, repositoryVariants: VariantRepository) =
            viewModelFactory {
                initializer { LobbyScreenViewModel(userInfoRepository, repositoryVariants) }
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
            val res = result.getOrNull()
            if (res == null)
                _state.value = FailedToFetch(Exception("Failed to fetch user info"))
            else
                _state.value = FetchedPlayerInfo(res)
        }
    }

    fun fetchMatchInfo(variant: Variants) {
        val value = _state.value
        check(value is FetchVariants && value.isFetched) {
            "Cannot fetch match info while loading"
        }
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

    fun fetchVariants() {
        if (_state.value !is FetchedPlayerInfo)
            throw IllegalStateException("Cannot fetch variants while loading")
        val userInfo = checkNotNull((_state.value as FetchedPlayerInfo).userInfo)
        _state.value = FetchVariants(
            false,
            null,
            userInfo
        )
        viewModelScope.launch {
            val result = kotlin.runCatching { repositoryVariants.getVariants() }
            val res = result.getOrNull()
            if (res == null)
                _state.value = FailedToFetchVariants(Exception("Failed to fetch variants"))
            else {
                Log.v(TAG, "Variants Received: $res")
                _state.value = FetchVariants(
                    true,
                    res,
                    userInfo
                )
            }

        }
    }
}