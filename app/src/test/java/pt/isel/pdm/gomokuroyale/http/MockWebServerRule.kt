package pt.isel.pdm.gomokuroyale.http

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import pt.isel.pdm.gomokuroyale.http.storage.UriDataStore

const val CALL_TIMEOUT_IN_SECS = 10L

/**
 * A JUnit rule that starts (and shuts down) a mock web to be used in tests.
 */
class MockWebServerRule : TestWatcher() {
//    val dataStore: DataStore<Preferences> = mock()
//
//    init {
//        val key = stringPreferencesKey("mock_web_server")
//        var mockPreferences: Preferences = mock {
//            on { this[key] }.thenReturn("mock_value")
//        }
//        val mockFlow: Flow<Preferences> = flowOf(mockPreferences)
//
//        whenever(dataStore.data).thenReturn(mockFlow)
//        runBlocking {
//            whenever(dataStore.updateData(any())).thenAnswer {
//                val updateAction = it.getArgument<(Preferences) -> Preferences>(0)
//                val updatedPreferences = updateAction(mockPreferences)
//                mockPreferences = updatedPreferences
//                updatedPreferences
//            }
//        }
//    }


    val webServer = MockWebServer()
    val httpClient: OkHttpClient =
        OkHttpClient.Builder()
            .callTimeout(CALL_TIMEOUT_IN_SECS, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    val gson: Gson = Gson()

    override fun starting(description: Description) {
        super.starting(description)
        webServer.start()
    }

    override fun finished(description: Description) {
        super.finished(description)
        webServer.shutdown()
    }
}