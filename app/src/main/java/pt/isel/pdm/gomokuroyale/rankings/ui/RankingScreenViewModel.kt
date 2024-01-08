package pt.isel.pdm.gomokuroyale.rankings.ui

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
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FailedToFetch
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchedRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchingPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchingRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.Idle
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.WantsToGoToMatchHistory
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchedPlayersBySearch
import pt.isel.pdm.gomokuroyale.util.onFailureResult
import pt.isel.pdm.gomokuroyale.util.onSuccessResult

class RankingScreenViewModel(
    private val service: UserService,
    private val repository: UserInfoRepository
) : ViewModel() {

    private val _state = MutableStateFlow<RankingScreenState>(Idle)

    val state: Flow<RankingScreenState> get() = _state.asStateFlow()

    fun getPlayers(page: Int = 1) {
        check(_state.value is Idle || _state.value is FetchedRankingInfo || _state.value is FetchedPlayersBySearch) {
            "Cannot fetch players while loading"
        }
        _state.value = FetchingRankingInfo
        viewModelScope.launch {
            service.getRankingInfo(page).onSuccessResult { rankingList ->
                _state.value = FetchedRankingInfo(rankingList, page)
            }.onFailureResult {
                _state.value = FailedToFetch(it)
            }
        }
    }

    fun search(query: String, page: Int = 1) = _state.value.let { initState ->
        check(initState is FetchedRankingInfo) { "Cannot search while loading" }
        _state.value = FetchingPlayersBySearch
        viewModelScope.launch {
            service.getRankingInfoByUsername(query, page).onSuccessResult { rankingList ->
                _state.value =
                    FetchedPlayersBySearch(rankingList, initState.page)
            }.onFailureResult {
                _state.value = FailedToFetch(it)
            }
        }
    }

    fun getUserInfo(id: Int) = _state.value.let { value ->
        check(value is FetchedRankingInfo) { "Cannot fetch user info while loading" }
        _state.value = FetchingPlayerInfo
        viewModelScope.launch {
            val result = kotlin.runCatching { repository.getUserInfo() }.getOrNull()
            if (result == null)
                _state.value = FailedToFetch(Exception("User not logged in"))
            else
                service.getStatsById(id, result.accessToken).onSuccessResult { user ->
                    _state.value = FetchedPlayerInfo(user, value.page)
                }.onFailureResult {
                    _state.value = FailedToFetch(it)
                }
        }
    }

    fun goToMatchHistory(id: Int, username: String) = _state.value.let { value ->
        check(value is FetchedPlayerInfo) { "Cannot go to match history while loading" }
        _state.value = WantsToGoToMatchHistory(id, username, value.page)
    }

    /**
     * Resets the view model to the idle state. From the idle state, the user information
     * can be fetched again.
     */
    fun resetToIdle() {
        check(_state.value is Idle) { "Cannot reset to fetching ranking info while loading" }
        _state.value = FetchingRankingInfo
    }

    companion object {
        fun factory(rankingInfoService: UserService, userInfoRepository: UserInfoRepository) =
            viewModelFactory {
                initializer { RankingScreenViewModel(rankingInfoService, userInfoRepository) }
            }
    }
}

