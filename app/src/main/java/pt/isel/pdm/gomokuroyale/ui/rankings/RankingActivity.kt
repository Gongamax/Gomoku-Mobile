package pt.isel.pdm.gomokuroyale.ui.rankings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class RankingActivity: ComponentActivity() {
    companion object {
        fun navigateTo(origin : Activity){
            val intent = Intent(origin, RankingActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RankingScreen(
                onBackRequested = { finish() },
                onPlayerClicked = { getPlayer(it) },
                rankingTable = getTop10()
            )
        }
    }

    private fun getTop10(): RankingTable {
            return RankingTable(leaderboard)
    }

    private fun getPlayer(it: String) {
        TODO("Not yet implemented")
    }
}