package pt.isel.pdm.gomokuroyale.rankings.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.http.services.users.UserService
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchedRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchingPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FailedToFetchRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchingRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.Idle
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingState
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.WantsToGoToMatchHistory
import pt.isel.pdm.gomokuroyale.util.onFailureResult
import pt.isel.pdm.gomokuroyale.util.onSuccessResult

class RankingScreenViewModel(
    private val service: UserService,
    private val repository: UserInfoRepository
) : ViewModel() {

    private val _state = MutableStateFlow<RankingScreenState>(Idle)

    val state: Flow<RankingScreenState> get() = _state.asStateFlow()

    fun getPlayers(page: Int = 1) {
//        check(_state.value is Idle || _state.value is FetchedRankingInfo) {
//            "Cannot fetch players while loading, is on state ${_state.value}"
//        }
        Log.v("RANKING_ACTIVITY_TAG", "page: $page")
        _state.value = FetchingRankingInfo
        viewModelScope.launch {
            service.getRankingInfo(page).onSuccessResult { rankingList ->
                Log.v("RANKING_ACTIVITY_TAG", "state: ${_state.value}")
//                val existingPlayers = (_state.value as? FetchedRankingInfo)?.rankingInfo?.rank ?: emptyList()
//                Log.v("RANKING_ACTIVITY_TAG", "existing players: $existingPlayers")
//                val newPlayers = existingPlayers + rankingList
                _state.value = FetchedRankingInfo(rankingInfo = RankingState(rankingList), page)
            }.onFailureResult {
                _state.value = FailedToFetchRankingInfo(it)
            }
        }
    }

    fun search(query: String) {
        if (_state.value !is FetchingPlayersBySearch)
            throw IllegalStateException("Cannot search while loading")
        _state.value = FetchingPlayersBySearch
        viewModelScope.launch {
            val result = service.getRankingInfo(1)
            val search = result
//            _state.value = FetchedPlayersBySearch(RankingState(search.properties.rankingTable))
        }
    }

    fun getUserInfo(id: Int) {
        val value = _state.value
        if (value !is FetchedRankingInfo)
            throw IllegalStateException("Cannot fetch user info while loading")
        _state.value = FetchingPlayerInfo
        viewModelScope.launch {
            val result = kotlin.runCatching { checkNotNull(repository.getUserInfo()) }.getOrNull()
            if (result == null)
                _state.value = FailedToFetchRankingInfo(Exception())
            else
                service.getStatsById(id, result.accessToken).onSuccessResult { user ->
                    _state.value = FetchedPlayerInfo(user, value.page)
                }.onFailureResult {
                    _state.value = FailedToFetchRankingInfo(it)
                }
        }
    }

    fun goToMatchHistory(id: Int, username: String) {
        val value = _state.value
        if (value !is FetchedPlayerInfo)
            throw IllegalStateException("Cannot go to match history while loading")
        _state.value = WantsToGoToMatchHistory(id, username, value.page)
    }

    /**
     * Resets the view model to the idle state. From the idle state, the user information
     * can be fetched again.
     */
    fun resetToFetchingRankingInfo() {
        if (_state.value !is FetchedRankingInfo)
            throw IllegalStateException("Cannot reset to fetching ranking info while loading")
        _state.value = FetchingRankingInfo
    }

    companion object {
        fun factory(rankingInfoService: UserService, userInfoRepository: UserInfoRepository) =
            viewModelFactory {
                initializer { RankingScreenViewModel(rankingInfoService, userInfoRepository) }
            }
    }
}

