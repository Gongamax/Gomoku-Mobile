package pt.isel.pdm.gomokuroyale.authentication

import android.content.Context
import pt.isel.pdm.gomokuroyale.authentication.domain.UsersRepository

class UserInfoSharedPrefs(private val context: Context) : UsersRepository {

    private val prefs by lazy {
        context.getSharedPreferences(USER_INFO_PREFS, Context.MODE_PRIVATE)
    }

    override val accessToken: String?
        get() = prefs.getString(ACCESS_TOKEN, null)

    override val refreshToken: String?
        get() = prefs.getString(REFRESH_TOKEN, null)

    override val username: String?
        get() = prefs.getString(USERNAME, null)

    override fun login(
        accessToken: String,
        refreshToken: String,
        username: String,
    ) {
        prefs.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .putString(REFRESH_TOKEN, refreshToken)
            .putString(USERNAME, username)
            .apply()
    }

    override fun logout() {
        prefs.edit()
            .remove(ACCESS_TOKEN)
            .remove(REFRESH_TOKEN)
            .remove(USERNAME)
            .apply()
    }

    companion object {
        private const val ACCESS_TOKEN = "accessToken"
        private const val REFRESH_TOKEN = "refreshToken"
        private const val USERNAME = "username"
        private const val USER_INFO_PREFS = "UserInfoPrefs"
    }
}