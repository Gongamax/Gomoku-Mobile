package pt.isel.pdm.gomokuroyale.main.domain

import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.http.domain.Recipe

sealed class MainScreenState {
    data object Idle : MainScreenState()
    data object FetchingRecipes : MainScreenState()
    data class FetchedRecipes(val recipes: Result<List<Recipe>>) : MainScreenState()
    data class FailedToFetchRecipes(val error: Throwable) : MainScreenState()
    data object FetchingPlayerInfo : MainScreenState()
    data class FetchedPlayerInfo(val userInfo: Result<UserInfo?>) : MainScreenState()
    data class FetchVariant(val isFetched: Boolean, val variants: Result<List<Variant>>? = null) :
        MainScreenState()

    data class  FailedToFetchVariants(val error: Throwable) : MainScreenState()

    data object LoggingOut : MainScreenState()
    data class LoggedOut(val result: Result<Unit>) : MainScreenState()

    data class FailedToLogout(val error: Throwable) : MainScreenState()
}