package pt.isel.pdm.gomokuroyale.http

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.http.dto.DTO
import pt.isel.pdm.gomokuroyale.http.dto.GetHomeOutputModel
import pt.isel.pdm.gomokuroyale.http.media.siren.SirenModel
import pt.isel.pdm.gomokuroyale.http.services.games.GameService
import pt.isel.pdm.gomokuroyale.http.services.users.UserService
import pt.isel.pdm.gomokuroyale.http.utils.HTTPService
import pt.isel.pdm.gomokuroyale.http.utils.Uris

class GomokuService(
    client: OkHttpClient,
    gson: Gson,
    apiEndpoint: String
) : HTTPService(client, gson, apiEndpoint) {

    val userService = UserService(client, gson, apiEndpoint)

    val gameService = GameService(client, gson, apiEndpoint)

    //TODO: FIX THIS
    suspend fun getHome(): Result<SirenModel<Unit>> =
        get(path = Uris.HOME, responseType = GetHomeOutputModel::class.java)
}