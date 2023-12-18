package pt.isel.pdm.gomokuroyale.http.services.games

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.http.domain.games.Game
import pt.isel.pdm.gomokuroyale.http.domain.games.MatchmakeStatus
import pt.isel.pdm.gomokuroyale.http.domain.games.QueueEntry
import pt.isel.pdm.gomokuroyale.http.services.HTTPService
import pt.isel.pdm.gomokuroyale.http.services.games.dto.CancelMatchmakingOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameGetAllByUserOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameGetByIdOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingInputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingStatusOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GamePlayInputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.SurrenderGameOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.models.CancelMatchmakingOutput
import pt.isel.pdm.gomokuroyale.http.utils.Rels
import pt.isel.pdm.gomokuroyale.util.ApiError
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.util.onError
import pt.isel.pdm.gomokuroyale.util.onSuccess

class GameService(
    client: OkHttpClient,
    gson: Gson,
    apiEndpoint: String,
    uriRepository: UriRepository
) : HTTPService(client, gson, apiEndpoint, uriRepository) {

    suspend fun getGame(gameId: Int, token: String): HttpResult<Game> {
        val path = uriRepository.getRecipeLink(Rels.GAME) ?: return HttpResult.Failure(
            ApiError("Game link not found")
        )
        val response = get<GameGetByIdOutputModel>(
            path = path.href.replace("{gid}", gameId.toString()),
            token = token,
        )
        return response.onSuccess {
            HttpResult.Success(
                Game(
                    id = it.properties.game.id,
                    players = Pair(it.properties.game.userBlack, it.properties.game.userWhite),
                    board = it.properties.game.board,
                    state = it.properties.game.state,
                    variant = it.properties.game.variant
                )
            )
        }.onError {
            val message = it.message ?: "Unknown error"
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun play(
        gameId: Int,
        token: String,
        play: GamePlayInputModel
    ): HttpResult<Game> {
        val path = uriRepository.getRecipeLink(Rels.GAME) ?: return HttpResult.Failure(
            ApiError("Game link not found")
        )
        val response = post<GameGetByIdOutputModel>(
            path = path.href.replace("{gid}", gameId.toString()),
            token = token,
            body = play
        )
        return response.onSuccess {
            HttpResult.Success(
                Game(
                    id = it.properties.game.id,
                    players = Pair(it.properties.game.userBlack, it.properties.game.userWhite),
                    board = it.properties.game.board,
                    state = it.properties.game.state,
                    variant = it.properties.game.variant
                )
            )
        }.onError {
            val message = it.message ?: "Unknown error"
            HttpResult.Failure(ApiError(message))
        }
    }


    suspend fun surrender(gameId: Int, token: String): HttpResult<Unit> {
        val path = uriRepository.getRecipeLink(Rels.GAME) ?: return HttpResult.Failure(
            ApiError("Game link not found")
        )
        val response = delete<SurrenderGameOutputModel>(
            path = path.href.replace("{gid}", gameId.toString()),
            token = token,
        )
        return response.onSuccess {
            HttpResult.Success(Unit)
        }.onError {
            val message = it.message ?: "Unknown error"
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getUserGames(token: String, userId: Int): HttpResult<List<Game>> {
        val path = uriRepository.getRecipeLink(Rels.GAME) ?: return HttpResult.Failure(
            ApiError("Game link not found")
        )
        val response = get<GameGetAllByUserOutputModel>(
            path = path.href.replace("{uid}", userId.toString()),
            token = token,
        )
        return response.onSuccess {
            HttpResult.Success(
                it.properties.games.map { game ->
                    Game(
                        id = game.id,
                        players = Pair(game.userBlack, game.userWhite),
                        board = game.board,
                        state = game.state,
                        variant = game.variant
                    )
                }
            )
        }.onError {
            val message = it.message ?: "Unknown error"
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun matchmaking(
        token: String,
        input: GameMatchmakingInputModel
    ): HttpResult<QueueEntry> {
        val path = uriRepository.getRecipeLink(Rels.MATCHMAKING) ?: return HttpResult.Failure(
            ApiError("Matchmaking link not found")
        )
        val response = post<GameMatchmakingOutputModel>(
            path = path.href,
            token = token,
            body = input
        )
        return response.onSuccess {
            HttpResult.Success(
                QueueEntry(
                    id = it.properties.id,
                    idType = it.properties.idType,
                    message = it.properties.message
                )
            )
        }.onError {
            val message = it.message ?: "Unknown error"
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun cancelMatchmaking(token: String, id: Int): HttpResult<Unit> {
        val path = uriRepository.getRecipeLink(Rels.MATCHMAKING) ?: return HttpResult.Failure(
            ApiError("Matchmaking link not found")
        )
        val response = delete<CancelMatchmakingOutputModel>(
            path = path.href.replace("{id}", id.toString()),
            token = token,
        )
        return response.onSuccess {
            HttpResult.Success(Unit)
        }.onError {
            val message = it.message ?: "Unknown error"
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getMatchmakingStatus(token: String, id: Int): HttpResult<MatchmakeStatus> {
        val path = uriRepository.getRecipeLink(Rels.MATCHMAKING) ?: return HttpResult.Failure(
            ApiError("Matchmaking link not found")
        )
        val response = get<GameMatchmakingStatusOutputModel>(
            path = path.href.replace("{id}", id.toString()),
            token = token,
        )
        return response.onSuccess {
            HttpResult.Success(
                MatchmakeStatus(
                    mid = it.properties.mid,
                    uid = it.properties.uid,
                    gid = it.properties.gid,
                    state = it.properties.state,
                    variant = it.properties.variant,
                    created = it.properties.created,
                    pollingTimOut = it.properties.pollingTimOut
                )
            )
        }.onError {
            val message = it.message ?: "Unknown error"
            HttpResult.Failure(ApiError(message))
        }
    }
}


