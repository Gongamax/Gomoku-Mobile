package pt.isel.pdm.gomokuroyale.http.utils

import java.net.URI

object Uris {

    private const val PREFIX = "api" //TODO: REVISE THIS /api or just api
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

//        fun createUser(): URI = URI(CREATE_USER)
//        fun token(): URI = URI(TOKEN)
//        fun logout(): URI = URI(LOGOUT)
//        fun getUserById(id: Int): URI = URI(GET_USER_BY_ID.replace("{id}", id.toString()))
//        fun authHome(): URI = URI(AUTH_HOME)
//        fun rankingInfo(): URI = URI(RANKING_INFO)
//        fun getStatsById(id: Int): URI = URI(GET_STATS_BY_ID.replace("{id}", id.toString()))

        fun createUser() : String = CREATE_USER
        fun token() : String = TOKEN
        fun logout() : String = LOGOUT
        fun getUserById(id: Int) : String = GET_USER_BY_ID.replace("{id}", id.toString())
        fun authHome() : String = AUTH_HOME
        fun rankingInfo() : String = RANKING_INFO
        fun getStatsById(id: Int) : String = GET_STATS_BY_ID.replace("{id}", id.toString())
    }

    object Games {
        const val GET_GAME_BY_ID = "$PREFIX/games/{id}"
        const val PLAY = "$PREFIX/games/{id}/play"
        const val MATCHMAKING = "$PREFIX/games/matchmaking"
        const val GET_MATCHMAKING_STATUS = "$PREFIX/games/matchmaking/{mid}/status"
        const val LEAVE = "$PREFIX/games/{id}/leave"
        const val GET_ALL_GAMES_BY_USER = "$PREFIX/games/user/{uid}"
        const val EXIT_MATCHMAKING_QUEUE = "$PREFIX/games/matchmaking/{mid}/exit"


//        fun getGame(id: Int): URI = URI(GET_GAME_BY_ID.replace("{id}", id.toString()))
//        fun play(id: Int): URI = URI(PLAY.replace("{id}", id.toString()))
//        fun matchmaking(): URI = URI(MATCHMAKING)
//        fun getMatchmakingStatus(id: Int): URI =
//            URI(GET_MATCHMAKING_STATUS.replace("{mid}", id.toString()))
//        fun leave(id: Int): URI = URI(LEAVE.replace("{id}", id.toString()))
//        fun getAllGamesByUser(id: Int): URI =
//            URI(GET_ALL_GAMES_BY_USER.replace("{uid}", id.toString()))
//        fun exitMatchmakingQueue(id: Int): URI =
//            URI(EXIT_MATCHMAKING_QUEUE.replace("{mid}", id.toString()))

        fun getGame(id: Int): String = GET_GAME_BY_ID.replace("{id}", id.toString())
        fun play(id: Int): String = PLAY.replace("{id}", id.toString())
        fun matchmaking(): String = MATCHMAKING
        fun getMatchmakingStatus(id: Int): String =
            GET_MATCHMAKING_STATUS.replace("{mid}", id.toString())
        fun leave(id: Int): String = LEAVE.replace("{id}", id.toString())
        fun getAllGamesByUser(id: Int): String =
            GET_ALL_GAMES_BY_USER.replace("{uid}", id.toString())
        fun exitMatchmakingQueue(id: Int): String =
            EXIT_MATCHMAKING_QUEUE.replace("{mid}", id.toString())
    }
}
