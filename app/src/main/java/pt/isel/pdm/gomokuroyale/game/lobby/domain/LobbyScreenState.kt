package pt.isel.pdm.gomokuroyale.game.lobby.domain

import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant

/**
 * Represents the state of the lobby screen.
 * It can be in one of the following states:
 * - [FetchingPlayerInfo]
 * - [FetchedPlayerInfo]
 * - [FailedToFetch]
 * - [FetchingMatchInfo]
 * - [FetchedMatchInfo]
 */
sealed class LobbyScreenState

data object FetchingPlayerInfo : LobbyScreenState()

data class FetchedPlayerInfo(val userInfo: UserInfo?) : LobbyScreenState()

data class FailedToFetch(val error: Throwable) : LobbyScreenState()

data class FetchVariants(
    val isFetched: Boolean,
    val variants: List<Variant>? = null,
    val userInfo: UserInfo
) : LobbyScreenState()

data class FailedToFetchVariants(val error: Throwable) : LobbyScreenState()

data class FetchingMatchInfo(val userInfo: UserInfo) : LobbyScreenState()

data class FetchedMatchInfo(val matchInfo: MatchInfo?) : LobbyScreenState()


