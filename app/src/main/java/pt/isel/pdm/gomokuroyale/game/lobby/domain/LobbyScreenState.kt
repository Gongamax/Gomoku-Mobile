package pt.isel.pdm.gomokuroyale.game.lobby.domain

import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo

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

data class FetchedPlayerInfo(val userInfo : UserInfo?) : LobbyScreenState()

data class FailedToFetch(val error : Throwable) : LobbyScreenState()

data class FetchingMatchInfo(val userInfo: UserInfo) : LobbyScreenState()

data class FetchedMatchInfo(val matchInfo : MatchInfo?) : LobbyScreenState()
