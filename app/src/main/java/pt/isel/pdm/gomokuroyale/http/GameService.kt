package pt.isel.pdm.gomokuroyale.http

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.http.dto.DTO
import pt.isel.pdm.gomokuroyale.http.dto.GameEmptyOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.GameGetAllByUserOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.GameMatchmakingInputModel
import pt.isel.pdm.gomokuroyale.http.dto.GameMatchmakingStatusOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.GameOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.GamePlayInputModel
import pt.isel.pdm.gomokuroyale.http.utils.HTTPService
import pt.isel.pdm.gomokuroyale.http.utils.Uris

class GameService(
    client: OkHttpClient,
    gson: Gson,
    apiEndpoint: String
) : HTTPService(client, gson, apiEndpoint) {

    suspend fun getGame(gameId: String, token: String): Result<DTO> =
        get(
            path = Uris.Games.GET_GAME_BY_ID,
            token = token,
            responseType = GameOutputModel::class.java
        )

    suspend fun play(
        gameId: String,
        token: String,
        play: GamePlayInputModel
    ): Result<DTO> =
        put(
            path = Uris.Games.PLAY,
            token = token,
            responseType = GameOutputModel::class.java,
            body = play
        )


    suspend fun surrender(gameId: String, token: String): Result<DTO> =
        put(
            path = Uris.Games.LEAVE,
            token = token,
            responseType = GameEmptyOutputModel::class.java,
            body = gameId
        )

    suspend fun getUserGames(token: String): Result<DTO> =
        get(
            path = Uris.Games.GET_ALL_GAMES_BY_USER,
            responseType = GameGetAllByUserOutputModel::class.java,
            token = token
        )

    suspend fun matchmaking(token: String, input: GameMatchmakingInputModel): Result<DTO> =
        post(
            path = Uris.Games.MATCHMAKING,
            responseType = GameOutputModel::class.java,
            token = token,
            body = input
        )

    suspend fun cancelMatchmaking(token: String): Result<DTO> =
        delete(
            path = Uris.Games.EXIT_MATCHMAKING_QUEUE,
            responseType = GameEmptyOutputModel::class.java,
            token = token,
            body = ""
        )

    suspend fun getMatchmakingStatus(token: String): Result<DTO> =
        get(
            path = Uris.Games.GET_MATCHMAKING_STATUS,
            token = token,
            responseType = GameMatchmakingStatusOutputModel::class.java
        )
}
