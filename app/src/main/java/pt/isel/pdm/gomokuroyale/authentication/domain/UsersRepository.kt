package pt.isel.pdm.gomokuroyale.authentication.domain

interface UsersRepository {
    val accessToken: String?
    val refreshToken: String?
    val username: String?

    val isLoggedIn: Boolean get() = accessToken != null

    fun login(
        accessToken: String,
        refreshToken: String,
        username: String,
    ): Unit

    fun logout(): Unit
}