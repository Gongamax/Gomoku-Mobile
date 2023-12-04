package pt.isel.pdm.gomokuroyale.http

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.http.services.users.UserService
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateInputModel
import pt.isel.pdm.gomokuroyale.http.utils.FetchFromAPIException

@OptIn(ExperimentalCoroutinesApi::class)
class HTTPServiceTests {

    @get:Rule
    val rule = MockWebServerRule()

    @Test
    fun `test register`() {
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
            apiEndpoint = rule.webServer.url("/").toString()
        )
        // Act
        val actual = runBlocking {
            sut.register(registerDTO.username, registerDTO.email, registerDTO.password)
        }
        // Assert
        assertEquals(2, actual.properties.uid)
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
            apiEndpoint = rule.webServer.url("/").toString()
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
            apiEndpoint = rule.webServer.url("/").toString()
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
            apiEndpoint = rule.webServer.url("/").toString()
        )

        //Act
        runBlocking { sut.getRankingInfo(0) }
    }
}