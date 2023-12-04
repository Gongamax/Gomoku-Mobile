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

    suspend fun getGame(gameId: String, token: String): GetGameOutput =
        get(
            path = Uris.Games.GET_GAME_BY_ID,
            token = token,
        )

    suspend fun play(
        gameId: String,
        token: String,
        play: GamePlayInputModel
    ): PlayGameOutput =
        put(
            path = Uris.Games.PLAY,
            token = token,
            body = play
        )


    suspend fun surrender(gameId: String, token: String): SurrenderGameOutput =
        put(
            path = Uris.Games.LEAVE,
            token = token,
            body = gameId
        )

    suspend fun getUserGames(token: String): GetUserGamesOutput =
        get(
            path = Uris.Games.GET_ALL_GAMES_BY_USER,
            token = token
        )

    suspend fun matchmaking(token: String, input: GameMatchmakingInputModel): MatchmakingOutput =
        post(
            path = Uris.Games.MATCHMAKING,
            token = token,
            body = input
        )

    suspend fun cancelMatchmaking(token: String): CancelMatchmakingOutput =
        delete(
            path = Uris.Games.EXIT_MATCHMAKING_QUEUE,
            token = token
        )

        suspend fun getMatchmakingStatus(token: String): GetMatchmakingStatusOutput =
        get(
            path = Uris.Games.GET_MATCHMAKING_STATUS,
            token = token,
        )
}


