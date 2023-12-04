package pt.isel.pdm.gomokuroyale.http.services.users

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.http.services.HTTPService
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateInputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateTokenInputModel
import pt.isel.pdm.gomokuroyale.http.services.users.models.GetRankingOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.GetStatsOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.GetUserHomeOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.GetUserOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.LoginOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.LogoutOutput
import pt.isel.pdm.gomokuroyale.http.services.users.models.RegisterOutput
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
    ): RegisterOutput {
        val res: RegisterOutput = post(path = Uris.Users.CREATE_USER, body = UserCreateInputModel(username, email, password))
        return res
    }


    suspend fun login(username: String, password: String): LoginOutput =
        post(path = Uris.Users.TOKEN, body = UserCreateTokenInputModel(username, password))

    suspend fun logout(token: String): LogoutOutput =
        post(path = Uris.Users.LOGOUT, token = token,)

    suspend fun getUser(id: Int): GetUserOutput =
        get(path = Uris.Users.GET_USER_BY_ID,)

    suspend fun updateUser() {
        // TODO
    }

    suspend fun getAuthHome(token: String): GetUserHomeOutput =
        get(path = Uris.Users.AUTH_HOME, token = token,)

    suspend fun getStatsById(id: Int, token: String): GetStatsOutput =
        get(path = Uris.Users.GET_STATS_BY_ID, token = token,)

    suspend fun getRankingInfo(page: Int): GetRankingOutput =
        get(path = Uris.Users.RANKING_INFO + "?page=" + page)

}
