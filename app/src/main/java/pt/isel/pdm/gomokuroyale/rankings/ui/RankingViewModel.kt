package pt.isel.pdm.gomokuroyale.rankings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.http.domain.RankingEntry
import pt.isel.pdm.gomokuroyale.http.services.users.UserService
import pt.isel.pdm.gomokuroyale.rankings.domain.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.FetchedPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.domain.FetchedRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.FetchingPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.domain.FetchingRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingState
import pt.isel.pdm.gomokuroyale.rankings.domain.WantsToGoToMatchHistory

class RankingViewModel (private val service: UserService) : ViewModel() {

    companion object {
       fun factory(rankingInfoService: UserService) = viewModelFactory {
              initializer { RankingViewModel(rankingInfoService) }
       }
    }

    private val _state = MutableStateFlow<RankingScreenState>(FetchingRankingInfo)

    val state: Flow<RankingScreenState> get() = _state.asStateFlow()
    fun getPage(page: Int = 0) {
        if (_state.value !is FetchingRankingInfo)
            throw IllegalStateException("The view model is not in the idle state.")
        _state.value = FetchingRankingInfo
        viewModelScope.launch {
            val result =  service.getRankingInfo(page)
            val rankingInfo = result
//            _state.value = FetchedRankingInfo(RankingState( rankingInfo.properties.rankingTable))
        }
    }


    /*  AVISO:
    * A FUNÇÂO NÂO ESTÀ TOTALMENTE IMPLEMENTADA, FALTA CRIAR UMA FUNÇÂO NO SERVIÇO QUE FAÇA
    * A PESQUISA DOS UTILIZADORES QUE CONTENHAM O QUERY NO NOME
    * */
    fun search(query: String) {
        if (_state.value !is FetchingPlayersBySearch)
            throw IllegalStateException("Cannot search while loading")
        _state.value = FetchingPlayersBySearch
        viewModelScope.launch{
            val result = service.getRankingInfo(1)
            val search = result
//            _state.value = FetchedPlayersBySearch(RankingState(search.properties.rankingTable))
        }
    }

    fun getUserInfo(id: Int) {
        if (_state.value !is FetchingPlayerInfo)
            throw IllegalStateException("Cannot fetch user info while loading")
        _state.value = FetchingPlayerInfo
        viewModelScope.launch {
            val result = service.getStatsById(id, "token")
            val userInfo = result
//            _state.value = FetchedPlayerInfo(
//                RankingEntry(
//                    userInfo.properties.id,
//                    userInfo.properties.username,
//                    userInfo.properties.gamesPlayed,
//                    userInfo.properties.wins,
//                    userInfo.properties.losses,
//                    userInfo.properties.points,
//                    userInfo.properties.rank
//                ),
//            )
        }
    }

    fun goToMatchHistory(id: Int, username: String) {
        if (_state.value !is FetchedPlayerInfo)
            throw IllegalStateException("Cannot go to match history while loading")
        _state.value = WantsToGoToMatchHistory(id, username)
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
}

