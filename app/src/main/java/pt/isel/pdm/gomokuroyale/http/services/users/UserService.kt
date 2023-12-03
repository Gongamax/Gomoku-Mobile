package pt.isel.pdm.gomokuroyale.http.services.users

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.http.services.users.dto.RankingInfoOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateInputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateTokenInputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserEmptyOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserGetByIdOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserHomeOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserStatsOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserTokenCreateOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserTokenRemoveOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.models.GetRankingOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.GetStatsOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.GetUserHomeOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.GetUserOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.LoginOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.LogoutOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.RegisterOutput
import pt.isel.pdm.gomokuroyale.http.utils.HTTPService
import pt.isel.pdm.gomokuroyale.http.utils.Uris

//TODO: CHANGE OUTPUT MODELS TO DOMAIN MODELS
class UserService(
    client: OkHttpClient,
    gson: Gson,
    apiEndpoint: String
) : HTTPService(client, gson, apiEndpoint) {
    suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<RegisterOutput> =
        post(
            path = Uris.Users.CREATE_USER,
            responseType = UserEmptyOutputModel::class.java,
            body = UserCreateInputModel(username, email, password)
        )

    suspend fun login(username: String, password: String): Result<LoginOutput> =
        post(
            path = Uris.Users.TOKEN,
            responseType = UserTokenCreateOutputModel::class.java,
            body = UserCreateTokenInputModel(username, password)
        )

    suspend fun logout(token: String): Result<LogoutOutput> =
        post(
            path = Uris.Users.LOGOUT,
            token = token,
            responseType = UserTokenRemoveOutputModel::class.java
        )

    suspend fun getUser(id: Int): Result<GetUserOutput> =
        get(
            path = Uris.Users.GET_USER_BY_ID,
            responseType = UserGetByIdOutputModel::class.java
        )

    suspend fun updateUser() {
        // TODO
    }

    suspend fun getAuthHome(token: String): Result<GetUserHomeOutput> =
        get(
            path = Uris.Users.AUTH_HOME,
            token = token,
            responseType = UserHomeOutputModel::class.java
        )

    suspend fun getStatsById(id: Int, token: String): Result<GetStatsOutput> =
        get(
            path = Uris.Users.GET_STATS_BY_ID,
            token = token,
            responseType = UserStatsOutputModel::class.java
        )

    suspend fun getRankingInfo(page: Int): Result<GetRankingOutput> =
        get(
            path = "/api/ranking?page=$page",
            responseType = RankingInfoOutputModel::class.java
        )

}
