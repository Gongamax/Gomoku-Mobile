package pt.isel.pdm.gomokuroyale.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.game.play.ui.GameActivity
import pt.isel.pdm.gomokuroyale.about.ui.AboutActivity
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginActivity
import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterActivity
import pt.isel.pdm.gomokuroyale.GomokuRoyaleApplication
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyActivity
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingActivity
import pt.isel.pdm.gomokuroyale.util.Idle
import pt.isel.pdm.gomokuroyale.util.Saved

const val TAG = "GOMOKU_ROYALE_TAG"

class MainActivity : ComponentActivity() {

    private val app by lazy { application as GomokuRoyaleApplication }

    private val dependencies by lazy { (application as DependenciesContainer) }

    private val viewModel by viewModels<MainScreenViewModel> {
        MainScreenViewModel.factory(
            dependencies.gomokuService,
            dependencies.uriRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate() called")

        //TODO: ADD TO CHECK IF USER IS LOGGED IN
        lifecycleScope.launch {
            viewModel.state.collect {
                if (it is Idle) {
                    viewModel.updateRecipes()
                }
            }
        }

        setContent {
            MainScreen(
                onLoginRequested = { LoginActivity.navigateTo(this) },
                onRegisterRequested = { RegisterActivity.navigateTo(this) },
                onCreateGameRequested = { LobbyActivity.navigateTo(this) },
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

