package pt.isel.pdm.gomokuroyale.authentication.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import pt.isel.pdm.gomokuroyale.authentication.domain.User
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository

class UserInfoDataStore(
    private val store: DataStore<Preferences>
) : UserInfoRepository {

    private val usernameKey = stringPreferencesKey(USER_NAME)
    private val accessToken = stringPreferencesKey(ACCESS_TOKEN)
//    private val refreshToken = stringPreferencesKey(REFRESH_TOKEN)
    private val points = stringPreferencesKey(POINTS)
    override val isLoggedIn: Boolean    //TODO: IMPROVE THIS CONDITION
        get() = store.data.toString().isNotEmpty()

    override suspend fun login(userInfo: UserInfo) {
        store.edit { preferences ->
            preferences[usernameKey] = userInfo.username
            preferences[accessToken] = userInfo.accessToken
//            preferences[refreshToken] = userInfo.refreshToken
        }
    }

    // Verification of the user's credentials
//    override suspend fun register(user: User) {
//        store.edit { preferences ->
//            preferences[usernameKey] = user.username
//            preferences[accessToken] = user.password
//        }
//    }
    override suspend fun logout() {
        store.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun getUserInfo(): UserInfo? {
        val preferences = store.data.first()
        val username = preferences[usernameKey] ?: return null
        val accessToken = preferences[accessToken] ?: return null
//        val refreshToken = preferences[refreshToken] ?: return null
        return UserInfo(accessToken,username)
    }

    companion object {
        private const val USER_NAME = "username"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val POINTS = "points"
    }
}