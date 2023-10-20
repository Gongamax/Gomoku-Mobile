package pt.isel.pdm.gomokuroyale.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import pt.isel.pdm.gomokuroyale.game.play.ui.GameActivity
import pt.isel.pdm.gomokuroyale.ui.about.AboutActivity
import pt.isel.pdm.gomokuroyale.ui.rankings.RankingActivity

const val TAG = "GOMOKU_ROYALE_TAG"

class MainActivity : ComponentActivity() {

//    private val service = GomokuService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate() called")
        setContent {
            MainScreen(
                onCreateGameRequested = { GameActivity.navigateTo(this) },
                onInfoRequested = { AboutActivity.navigateTo(this) },
                onRankingRequested = { RankingActivity.navigateTo(this) }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy() called")
    }
}

