package pt.isel.pdm.gomokuroyale.rankings.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.gomokuroyale.userStats.UserStatsActivity

class RankingActivity: ComponentActivity() {
    companion object {
        fun navigateTo(origin : Activity){
            val intent = Intent(origin, RankingActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs by lazy { applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE) }
        super.onCreate(savedInstanceState)
        setContent {
            RankingScreen(
                onBackRequested = { finish() },
                onPlayerClicked = { username -> UserStatsActivity.navigateTo(this, username) },
                rankingTable = getTop10(),
                prefs
            )
        }
    }

    private fun getTop10(): RankingTable {
            return RankingTable(leaderboard)
    }

}