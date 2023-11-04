package pt.isel.pdm.gomokuroyale.http.utils

import java.net.URI

object Uris {

    private const val PREFIX = "/api"
    const val HOME = PREFIX
    const val SYSTEM_INFO = "$PREFIX/system"
    fun home(): URI = URI(HOME)
    fun systemInfo(): URI = URI(SYSTEM_INFO)
    object Users {
        const val CREATE_USER = "$PREFIX/users"
        const val TOKEN = "$PREFIX/users/token"
        const val LOGOUT = "$PREFIX/logout"
        const val GET_USER_BY_ID = "$PREFIX/users/{id}"
        const val AUTH_HOME = "$PREFIX/me"
        const val RANKING_INFO = "$PREFIX/ranking"
        const val GET_STATS_BY_ID = "$PREFIX/stats/{id}"
    }

    object Games {
        const val CREATE_GAME = "$PREFIX/games"
        const val GET_GAME_BY_ID = "$PREFIX/games/{id}"
        const val PLAY = "$PREFIX/games/{id}/play"
        const val MATCHMAKING = "$PREFIX/games/matchmaking"
        const val GET_MATCHMAKING_STATUS = "$PREFIX/games/matchmaking/status"
        const val LEAVE = "$PREFIX/games/{id}/leave"
        const val GET_ALL_GAMES = "$PREFIX/games"
        const val GET_ALL_GAMES_BY_USER = "$PREFIX/games/user"
        const val EXIT_MATCHMAKING_QUEUE = "$PREFIX/games/matchmaking/exit"
    }
}
