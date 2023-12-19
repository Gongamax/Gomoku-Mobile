package pt.isel.pdm.gomokuroyale.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.domain.Recipe
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState.FailedToFetchRecipes
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState.FailedToLogout
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState.FetchedRecipes
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState.FetchingRecipes
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState.Idle
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState.LoggedOut
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState.LoggingOut
import pt.isel.pdm.gomokuroyale.util.onFailureResult
import pt.isel.pdm.gomokuroyale.util.onSuccessResult

class MainScreenViewModel(
    private val repository: UserInfoRepository,
    private val uriRepository: UriRepository,
    private val gomokuService: GomokuService
) : ViewModel() {

    private val _state: MutableStateFlow<MainScreenState> = MutableStateFlow(Idle)
    val state: Flow<MainScreenState> get() = _state.asStateFlow()

    fun updateRecipes() {
        _state.value = FetchingRecipes
        viewModelScope.launch {
            val response = gomokuService.getHome()
            response.onSuccessResult {
                val recipes = it.map { link ->
                    Recipe(getRelName(link.rel.first()), link.href)
                }
                val result =
                    kotlin.runCatching { uriRepository.updateRecipeLinks(recipes); recipes }
                _state.value = FetchedRecipes(result)
            }.onFailureResult {
                _state.value = FailedToFetchRecipes(it)
            }
        }
    }

    fun fetchPlayerInfo() {
        if (_state.value !is FetchedRecipes)
            throw IllegalStateException("The view model is not in the loading state.")
        _state.value = FetchingPlayerInfo
        viewModelScope.launch {
            val result = runCatching { repository.getUserInfo() }
            _state.value = FetchedPlayerInfo(result)
        }
    }

    fun logout(accessToken: String?) {
        if (_state.value !is FetchedPlayerInfo)
            throw IllegalStateException("The view model is not in the idle state.")
        if (accessToken == null)
            throw IllegalArgumentException("The access token cannot be null.")
        _state.value = LoggingOut
        viewModelScope.launch {
            gomokuService.userService.logout(accessToken).onSuccessResult {
                val result = runCatching { repository.logout() }
                _state.value = LoggedOut(result)
            }.onFailureResult {
                runCatching { repository.logout() }
                _state.value = FailedToLogout(it)
            }
        }
    }

    fun resetToIdle() {
//        if (_state.value !is FetchedRecipes || _state.value !is FetchedPlayerInfo || _state.value !is LoggedOut)
//            throw IllegalStateException("The view model is not in fetched state.")
        _state.value = Idle
    }

    private fun getRelName(relUrl: String) = relUrl.split(DELIMITER).last()

    companion object {
        fun factory(
            userInfoRepository: UserInfoRepository,
            gomokuService: GomokuService,
            uriRepository: UriRepository
        ) = viewModelFactory {
            initializer { MainScreenViewModel(userInfoRepository, uriRepository, gomokuService) }
        }

        private const val DELIMITER = "/"
    }
}