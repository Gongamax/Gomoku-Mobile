package pt.isel.pdm.gomokuroyale.game.play.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class GameActivity : ComponentActivity() {

    private val viewModel by viewModels<GameScreenViewModel>()

    companion object {
        fun navigateTo(context : Context) {
            with(context) {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (viewModel.game == null)
                viewModel.newGame() //hardcoded while we dont have a screen for creating a game
            GameScreen(
                board = viewModel.game!!.board, //temporary double bang while we dont have the middle screen
                onPlayRequested = viewModel::makeMove,
                onForfeitRequested = { finish() } //viewModel.forfeit()
            )
        }
    }
}