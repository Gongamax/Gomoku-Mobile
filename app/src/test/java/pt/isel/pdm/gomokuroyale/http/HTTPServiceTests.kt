package pt.isel.pdm.gomokuroyale.http

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import pt.isel.pdm.gomokuroyale.http.domain.Recipe
import pt.isel.pdm.gomokuroyale.http.services.users.UserService
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateInputModel
import pt.isel.pdm.gomokuroyale.http.storage.UriDataStore
import pt.isel.pdm.gomokuroyale.http.utils.FetchFromAPIException
import pt.isel.pdm.gomokuroyale.util.HttpResult

@OptIn(ExperimentalCoroutinesApi::class)
class HTTPServiceTests {

    @get:Rule
    val rule = MockWebServerRule()

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = TestScope(UnconfinedTestDispatcher()),
            produceFile = { tmpFolder.newFile("test.preferences_pb") }
        )

    @Test
    fun `test register`() = runTest {
        // Arrange
        val registerDTO = UserCreateInputModel(
            username = "username",
            email = "email@gmail.com",
            password = "Password123"
        )
        val response =
            rule.webServer.enqueue(
                MockResponse()
                    .setResponseCode(201)
                    .addHeader("Content-Type", "application/vnd.siren+json")
                    .setBody(
                        "{\n" +
                                "    \"class\": [\n" +
                                "        \"register\"\n" +
                                "    ],\n" +
                                "    \"properties\": {\n" +
                                "        \"uid\": 2\n" +
                                "    },\n" +
                                "    \"links\": [\n" +
                                "        {\n" +
                                "            \"rel\": [\n" +
                                "                \"self\"\n" +
                                "            ],\n" +
                                "            \"href\": \"/api/users\"\n" +
                                "        }\n" +
                                "    ],\n" +
                                "    \"entities\": [],\n" +
                                "    \"actions\": [],\n" +
                                "    \"requireAuth\": [\n" +
                                "        false\n" +
                                "    ]\n" +
                                "}"
                    )
            )
        val sut = UserService(
            client = rule.httpClient,
            gson = rule.gson,
            apiEndpoint = rule.webServer.url("/").toString(),
            uriRepository = UriDataStore(testDataStore)
        )
        sut.uriRepository.updateRecipeLinks(
            listOf(
                Recipe(
                    rel = "register",
                    href = "/api/users"
                )
        )
        )
        // Act
        val actual = runBlocking {
            sut.register(registerDTO.username, registerDTO.email, registerDTO.password)
        }

        // Assert
        when (actual) {
            is HttpResult.Success -> {
                assertEquals(2, actual.value.uid)
            }
            is HttpResult.Failure -> {
                fail("Should not be a failure, ${actual.error.message}")
            }
        }
    }

    @Test(expected = FetchFromAPIException::class)
    fun `fetch request on API throws FetchFromAPIException when API returns 500`() {
        //Arrange
        rule.webServer.enqueue(
            MockResponse().setResponseCode(500)
        )

        val sut = UserService(
            client = rule.httpClient,
            gson = rule.gson,
            apiEndpoint = rule.webServer.url("/").toString(),
            uriRepository = UriDataStore(testDataStore)
        )

        //Act
        runBlocking { sut.getRankingInfo(0) }
    }

    @Test
    fun `request on API throws CancellationException when coroutine is cancelled`() = runTest {
        //Arrange
        val sut = UserService(
            client = rule.httpClient,
            gson = rule.gson,
            apiEndpoint = rule.webServer.url("/").toString(),
            uriRepository = UriDataStore(testDataStore)
        )
        var cancellationThrown = false

        //Act
        val job = launch(UnconfinedTestDispatcher()) {
            try {
                sut.getRankingInfo(0)
            } catch (e: CancellationException) {
                cancellationThrown = true
                throw e
            }
        }
        job.cancelAndJoin()

        //Assert
        assertEquals(true, cancellationThrown)
    }

    @Test(expected = FetchFromAPIException::class)
    fun `request on API throws FetchFromAPIException on API access timeout`() {
        //Arrange
        val sut = UserService(
            client = rule.httpClient,
            gson = rule.gson,
            apiEndpoint = rule.webServer.url("/").toString(),
            uriRepository = UriDataStore(testDataStore)
        )
        //Act
        runBlocking { sut.getRankingInfo(0) }
    }
}