package pt.isel.pdm.gomokuroyale

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.authentication.storage.UserInfoDataStore
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.http.storage.UriDataStore
import java.util.concurrent.TimeUnit

/**
 * The tag used to identify log messages across the application. Here we elected to use the same
 * tag for all log messages.
 */
const val TAG = "GOMOKU_ROYALE_TAG"

class GomokuRoyaleApplication : Application(), DependenciesContainer {

    override val gson = Gson()

    override val client =
        OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .build()

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = GOMOKU_DATA_STORE)

    override val userInfoRepository: UserInfoRepository
        get() = UserInfoDataStore(dataStore)

    override val uriRepository: UriRepository
        get() = UriDataStore(dataStore)

    override val gomokuService: GomokuService
        get() = lazy { GomokuService(client, gson, API_ENDPOINT, uriRepository) }.value

    companion object {
        private const val API_ENDPOINT =
            "https://1cca-2001-8a0-f978-ae00-4851-cf37-4e53-dff2.ngrok-free.app" // API NGROK URL
        private const val GOMOKU_DATA_STORE = "gomoku_data_store"
    }
}