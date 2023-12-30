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
import pt.isel.pdm.gomokuroyale.game.lobby.domain.VariantRepository
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.domain.Recipe
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState.FailedToFetch
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
    private val gomokuService: GomokuService,
    private val variantRepository: VariantRepository
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
                _state.value = FailedToFetch(it)
            }
        }
    }

    fun updateVariants() {
        check(_state.value is FetchedRecipes) {
            "The view model is not in the fetched recipes state."
        }
        _state.value = MainScreenState.FetchingVariants
        viewModelScope.launch {
            val response = gomokuService.gameService.getVariants()
            response.onSuccessResult {
                val variants = it.map { variant ->
                    Variant(
                        variant.name,
                        variant.boardDim,
                        variant.playRule,
                        variant.openingRule,
                        variant.points
                    )
                }
                val result =
                    kotlin.runCatching { variantRepository.storeVariants(variants); variants }
                _state.value = MainScreenState.FetchedVariants(result)
            }.onFailureResult {
                _state.value = FailedToFetch(it)
            }
        }
    }


    fun fetchPlayerInfo() {
        if (_state.value !is MainScreenState.FetchedVariants && _state.value !is FetchedPlayerInfo)
            throw IllegalStateException("The view model cant fetch player info on ${_state.value} state.")
        _state.value = FetchingPlayerInfo
        viewModelScope.launch {
//            val isLogged = kotlin.runCatching { repository.isLoggedIn() }
//            if (isLogged.getOrNull() == false) {
//                _state.value = MainScreenState.FailedToToken(Exception("Failure of the token, time overrun"))
//                return@launch
//            }
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
        if (_state.value !is FailedToFetch && _state.value !is FailedToLogout)
            throw IllegalStateException("The view model is not in fetched state.")
        _state.value = Idle
    }

    private fun getRelName(relUrl: String) = relUrl.split(DELIMITER).last()

    companion object {
        fun factory(
            userInfoRepository: UserInfoRepository,
            gomokuService: GomokuService,
            uriRepository: UriRepository,
            variantRepository: VariantRepository
        ) = viewModelFactory {
            initializer {
                MainScreenViewModel(
                    userInfoRepository,
                    uriRepository,
                    gomokuService,
                    variantRepository
                )
            }
        }

        private const val DELIMITER = "/"
    }
}