package pt.isel.pdm.gomokuroyale

import android.app.Application
import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.http.GomokuService

class GomokuRoyaleApplication : Application(), GomokuRoyaleDependencyProvider {

    override val gson = Gson()

    override val client =
        OkHttpClient.Builder()
            .build()

    override val gomokuService = GomokuService(client, gson, API_ENDPOINT)

    companion object {
        private const val API_ENDPOINT = "API NGROK URL"
    }
}