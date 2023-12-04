package pt.isel.pdm.gomokuroyale.matchHistory.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.parcelize.Parcelize

class MatchHistoryActivity: ComponentActivity() {
    companion object {
        fun navigateTo(origin: Activity, playerId: Int, username: String) {
            with(origin) {
                val intent = Intent(origin, MatchHistoryActivity::class.java)
                intent.putExtra(USERS_EXTRA, UsersExtra(playerId, username))
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            getUserExtra()?.let { userExtra ->
                UserStatsScreen(
                    onBackRequested = { finish() },
                    username = userExtra.username
                )
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getUserExtra(): UsersExtra? =
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(USERS_EXTRA, UsersExtra::class.java)
        else
            intent.getParcelableExtra(USERS_EXTRA)

}

private const val USERS_EXTRA = "UserStatsActivity.extra.users"

@Parcelize
data class UsersExtra(val playerId: Int, val username: String) : Parcelable