package pt.isel.pdm.gomokuroyale.http.services.games

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameEmptyOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameGetAllByUserOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingInputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingStatusOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GamePlayInputModel
import pt.isel.pdm.gomokuroyale.http.services.games.models.CancelMatchmakingOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.GetGameOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.GetMatchmakingStatusOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.GetUserGamesOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.MatchmakingOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.PlayGameOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.SurrenderGameOutput
import pt.isel.pdm.gomokuroyale.http.utils.HTTPService
import pt.isel.pdm.gomokuroyale.http.utils.Uris

//TODO: CHANGE OUTPUT MODELS TO DOMAIN MODELS
class GameService(
    client: OkHttpClient,
    gson: Gson,
    apiEndpoint: String
) : HTTPService(client, gson, apiEndpoint) {

    suspend fun getGame(gameId: String, token: String): Result<GetGameOutput> =
        get(
            path = Uris.Games.GET_GAME_BY_ID,
            token = token,
            responseType = GameOutputModel::class.java
        )

    suspend fun play(
        gameId: String,
        token: String,
        play: GamePlayInputModel
    ): Result<PlayGameOutput> =
        put(
            path = Uris.Games.PLAY,
            token = token,
            responseType = GameOutputModel::class.java,
            body = play
        )


    suspend fun surrender(gameId: String, token: String): Result<SurrenderGameOutput> =
        put(
            path = Uris.Games.LEAVE,
            token = token,
            responseType = GameEmptyOutputModel::class.java,
            body = gameId
        )

    suspend fun getUserGames(token: String): Result<GetUserGamesOutput> =
        get(
            path = Uris.Games.GET_ALL_GAMES_BY_USER,
            responseType = GameGetAllByUserOutputModel::class.java,
            token = token
        )

    suspend fun matchmaking(token: String, input: GameMatchmakingInputModel): Result<MatchmakingOutput> =
        post(
            path = Uris.Games.MATCHMAKING,
            responseType = GameOutputModel::class.java,
            token = token,
            body = input
        )

    suspend fun cancelMatchmaking(token: String): Result<CancelMatchmakingOutput> =
        delete(
            path = Uris.Games.EXIT_MATCHMAKING_QUEUE,
            responseType = GameEmptyOutputModel::class.java,
            token = token
        )

        suspend fun getMatchmakingStatus(token: String): Result<GetMatchmakingStatusOutput> =
        get(
            path = Uris.Games.GET_MATCHMAKING_STATUS,
            token = token,
            responseType = GameMatchmakingStatusOutputModel::class.java
        )
}


