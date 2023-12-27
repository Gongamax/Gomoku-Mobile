package pt.isel.pdm.gomokuroyale

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.authentication.storage.UserInfoDataStore
import pt.isel.pdm.gomokuroyale.game.lobby.domain.VariantRepository
import pt.isel.pdm.gomokuroyale.game.lobby.storage.VariantDataStore
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Board
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.http.storage.UriDataStore
import pt.isel.pdm.gomokuroyale.util.BoardDeserializer
import java.util.concurrent.TimeUnit

/**
 * The tag used to identify log messages across the application. Here we elected to use the same
 * tag for all log messages.
 */
const val TAG = "GOMOKU_ROYALE_TAG"

class GomokuRoyaleApplication : Application(), DependenciesContainer {

    override val gson = GsonBuilder().registerTypeAdapter(Board::class.java, BoardDeserializer())
        .create()

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

    override val variantRepository: VariantRepository
        get() = VariantDataStore(dataStore, gson)

    companion object {
        private const val API_ENDPOINT =
            "https://81ab-2001-8a0-f978-ae00-69e7-360-99a5-5db7.ngrok-free.app" // API NGROK URL
        private const val GOMOKU_DATA_STORE = "gomoku_data_store"
    }
}