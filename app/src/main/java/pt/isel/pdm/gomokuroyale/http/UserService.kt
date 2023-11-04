package pt.isel.pdm.gomokuroyale.http

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.http.dto.RankingInfoOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.UserStatsOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.UserCreateInputModel
import pt.isel.pdm.gomokuroyale.http.dto.UserCreateTokenInputModel
import pt.isel.pdm.gomokuroyale.http.dto.UserEmptyOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.UserGetByIdOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.UserHomeOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.UserTokenCreateOutputModel
import pt.isel.pdm.gomokuroyale.http.dto.UserTokenRemoveOutputModel
import pt.isel.pdm.gomokuroyale.http.utils.HTTPService
import pt.isel.pdm.gomokuroyale.http.utils.Uris

class UserService(
    client: OkHttpClient,
    gson: Gson,
    apiEndpoint: String
) : HTTPService(client, gson, apiEndpoint) {
    suspend fun register(username: String, email: String, password: String) =
        post(
            path = Uris.Users.CREATE_USER,
            responseType = UserEmptyOutputModel::class.java,
            body = UserCreateInputModel(username, email, password)
        )

    suspend fun login(username: String, password: String) =
        post(
            path = Uris.Users.TOKEN,
            responseType = UserTokenCreateOutputModel::class.java,
            body = UserCreateTokenInputModel(username, password)
        )

    suspend fun logout(token: String) =
        post(
            path = Uris.Users.LOGOUT,
            token = token,
            responseType = UserTokenRemoveOutputModel::class.java
        )

    suspend fun getUser(id: Int) =
        get(
            path = Uris.Users.GET_USER_BY_ID,
            responseType = UserGetByIdOutputModel::class.java
        )

    suspend fun updateUser() {
        // TODO
    }

    suspend fun getAuthHome(token: String) =
        get(
            path = Uris.Users.AUTH_HOME,
            token = token,
            responseType = UserHomeOutputModel::class.java
        )

    suspend fun getStatsById(id: Int, token: String) =
        get(
            path = Uris.Users.GET_STATS_BY_ID,
            token = token,
            responseType = UserStatsOutputModel::class.java
        )

    suspend fun getRankingInfo() =
        get(
            path = Uris.Users.RANKING_INFO,
            responseType = RankingInfoOutputModel::class.java
        )

}
