package pt.isel.pdm.gomokuroyale.http.services.users

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.http.domain.users.UserId
import pt.isel.pdm.gomokuroyale.http.domain.users.UserRanking
import pt.isel.pdm.gomokuroyale.http.domain.users.UserToken
import pt.isel.pdm.gomokuroyale.http.services.HTTPService
import pt.isel.pdm.gomokuroyale.http.services.users.dto.RankingInfoOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateInputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateTokenInputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserStatsOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserTokenCreateOutputModel
import pt.isel.pdm.gomokuroyale.http.utils.Rels
import pt.isel.pdm.gomokuroyale.util.ApiError
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.util.onError
import pt.isel.pdm.gomokuroyale.util.onSuccess

class UserService(
    client: OkHttpClient,
    gson: Gson,
    apiEndpoint: String,
    uriRepository: UriRepository
) : HTTPService(client, gson, apiEndpoint, uriRepository) {

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): HttpResult<UserId> {
        val path = uriRepository.getRecipeLink(Rels.REGISTER) ?: return HttpResult.Failure(
            ApiError("Register link not found")
        )
        val response = post<UserCreateOutputModel>(
            path = path.href,
            body = UserCreateInputModel(username, email, password)
        )
        return response.onSuccess {
            HttpResult.Success(UserId(it.properties.uid))
        }.onError {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun login(username: String, password: String): HttpResult<UserToken> {
        val path = uriRepository.getRecipeLink(Rels.LOGIN) ?: return HttpResult.Failure(
            ApiError("Login link not found")
        )
        val response = post<UserTokenCreateOutputModel>(
            path = path.href,
            body = UserCreateTokenInputModel(username, password)
        )
        return response.onSuccess {
            HttpResult.Success(UserToken(it.properties.token))
        }.onError {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

//    suspend fun logout(token: String): HttpResult<LogoutOutput> {
//        val response = post<UserTokenRemoveOutputModel>(path = Uris.Users.logout(), token = token)
//        return response.onSuccess {
//            HttpResult.Success(it)
//        }.onError {
//            val message = it.message.errorMessage
//            HttpResult.Failure(ApiError(message))
//        }
//    }
//
//    suspend fun getUser(id: Int): HttpResult<User> {
//        val response = get<UserGetByIdOutputModel>(
//            path = Uris.Users.getUserById(id)
//        )
//        return response.onSuccess {
//            HttpResult.Success(
//                User(
//                    id = Id(it.properties.id),
//                    username = it.properties.username,
//                    email = Email(it.properties.email),
//                )
//            )
//        }.onError {
//            val message = it.message ?: "Unknown error"
//            HttpResult.Failure(ApiError(message))
//        }
//    }
//
//    suspend fun getAuthHome(token: String): HttpResult<UserHome> {
//        val response = get<UserHomeOutputModel>(
//            path = Uris.Users.authHome(),
//            token = token
//        )
//        return response.onSuccess {
//            HttpResult.Success(
//                UserHome(
//                    id = it.properties.id,
//                    username = it.properties.username,
//                )
//            )
//        }.onError {
//            val message = it.message.errorMessage
//            HttpResult.Failure(ApiError(message))
//        }
//    }

    suspend fun getStatsById(id: Int, token: String): HttpResult<UserRanking> {
        val path = uriRepository.getRecipeLink(Rels.USER_STATS) ?: return HttpResult.Failure(
            ApiError("User stats link not found")
        )
        val response = get<UserStatsOutputModel>(
            path = path.href.replace("{uid}", id.toString()),
            token = token
        )
        return response.onSuccess {
            HttpResult.Success(
                UserRanking(
                    id = it.properties.id,
                    username = it.properties.username,
                    gamesPlayed = it.properties.gamesPlayed,
                    wins = it.properties.wins,
                    losses = it.properties.losses,
                    rank = it.properties.rank,
                    points = it.properties.points
                )
            )
        }.onError {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    suspend fun getRankingInfo(page: Int): HttpResult<List<UserRanking>> {
        val path = uriRepository.getRecipeLink(Rels.RANKING_INFO) ?: return HttpResult.Failure(
            ApiError("Ranking link not found")
        )
        val response = get<RankingInfoOutputModel>(
            path = path.href.replace("{page}", page.toString())
        )
        return response.onSuccess {
            HttpResult.Success(
                it.entities.map { entity ->
                    val stats = entity as UserStatsOutputModel
                    UserRanking(
                        id = stats.id,
                        username = entity.username,
                        gamesPlayed = entity.gamesPlayed,
                        wins = entity.wins,
                        losses = entity.losses,
                        rank = entity.rank,
                        points = entity.points
                    )
                }
            )
        }.onError {
            val message = it.message.errorMessage
            HttpResult.Failure(ApiError(message))
        }
    }

    private val String?.errorMessage get () = this ?: unknownError
    private val unknownError get () = "Unknown error"
}
