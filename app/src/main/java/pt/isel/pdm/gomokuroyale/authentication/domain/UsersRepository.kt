package pt.isel.pdm.gomokuroyale.authentication.domain

data class UserInfo(val acessToken: String, val refreshToken: String, val username: String) {

    init {
        require(validateUserInfo(this))
    }

    private fun validateUserInfo(userInfo: UserInfo): Boolean {
        return userInfo.acessToken.isNotBlank() && userInfo.refreshToken.isNotBlank() &&
                userInfo.username.isNotBlank()
    }
}

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