package pt.isel.pdm.gomokuroyale

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.gomokuroyale.authentication.UserInfoSharedPrefs
import pt.isel.pdm.gomokuroyale.http.GomokuService

interface GomokuRoyaleDependencyProvider {
    val gson: Gson
    val client: OkHttpClient
    val userInfoSharedPrefs: UserInfoSharedPrefs
    val gomokuService: GomokuService
}