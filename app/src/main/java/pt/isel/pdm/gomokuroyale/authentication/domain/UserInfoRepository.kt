package pt.isel.pdm.gomokuroyale.authentication.domain

interface UserInfoRepository {

    val isLoggedIn: Boolean
    suspend fun login(userInfo: UserInfo): Unit

    suspend fun logout(): Unit
    suspend fun getUserInfo(): UserInfo?
}