package pt.isel.pdm.gomokuroyale.http

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateInputModel
import pt.isel.pdm.gomokuroyale.http.services.users.UserService

@OptIn(ExperimentalCoroutinesApi::class)
class UserServiceTests {

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
        rule.webServer.enqueue(
            MockResponse()
                .setResponseCode(201)
                .addHeader("Content-Type", "application/vnd.siren+json")
                .setBody(rule.gson.toJson(registerDTO))
        )
        val sut = UserService(
            client = rule.httpClient,
            gson = rule.gson,
            apiEndpoint = rule.webServer.url("/").toString()
        )
        // Act
        val actual = runBlocking { sut.register(registerDTO.username, registerDTO.email, registerDTO.password) }
        // Assert
        assert(actual.isSuccess)
    }

    @Test
    fun `test login`() {
        // TODO
    }

    @Test
    fun `test logout`() {
        // TODO
    }

    @Test
    fun `test getUser`() {
        // TODO
    }

    @Test
    fun `test updateUser`() {
        // TODO
    }

    //...
}