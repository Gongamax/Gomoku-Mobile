package pt.isel.pdm.gomokuroyale.http.services.games

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.http.services.HTTPService
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingInputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GamePlayInputModel
import pt.isel.pdm.gomokuroyale.http.services.games.models.CancelMatchmakingOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.GetGameOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.GetMatchmakingStatusOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.GetUserGamesOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.MatchmakingOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.PlayGameOutput
import pt.isel.pdm.gomokuroyale.http.services.games.models.SurrenderGameOutput
import pt.isel.pdm.gomokuroyale.http.utils.Uris

//TODO: CHANGE OUTPUT MODELS TO DOMAIN MODELS
class GameService(
    client: OkHttpClient,
    gson: Gson,
    apiEndpoint: String
) : HTTPService(client, gson, apiEndpoint) {

    suspend fun getGame(gameId: Int, token: String): GetGameOutput =
        get(
            path = Uris.Games.getGame(gameId),
            token = token,
        )

    suspend fun play(
        gameId: Int,
        token: String,
        play: GamePlayInputModel
    ): PlayGameOutput =
        put(
            path = Uris.Games.play(gameId),
            token = token,
            body = play
        )


    suspend fun surrender(gameId: Int, token: String): SurrenderGameOutput =
        put(
            path = Uris.Games.leave(gameId),
            token = token,
            body = gameId
        )

    suspend fun getUserGames(token: String, userId: Int): GetUserGamesOutput =
        get(
            path = Uris.Games.getAllGamesByUser(userId),
            token = token
        )

    suspend fun matchmaking(token: String, input: GameMatchmakingInputModel): MatchmakingOutput =
        post(
            path = Uris.Games.matchmaking(),
            token = token,
            body = input
        )

    suspend fun cancelMatchmaking(token: String, id: Int): CancelMatchmakingOutput =
        delete(
            path = Uris.Games.exitMatchmakingQueue(id),
            token = token
        )

    suspend fun getMatchmakingStatus(token: String, id: Int): GetMatchmakingStatusOutput =
        get(
            path = Uris.Games.getMatchmakingStatus(id),
            token = token,
        )
}


