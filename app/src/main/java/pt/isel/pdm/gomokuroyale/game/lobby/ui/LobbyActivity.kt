package pt.isel.pdm.gomokuroyale.game.lobby.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class LobbyActivity : ComponentActivity() {

    private val viewModel by viewModels<LobbyScreenViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LobbyScreen()
        }
    }


}