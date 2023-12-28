package pt.isel.pdm.gomokuroyale.main.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.about.ui.AboutActivity
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginActivity
import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterActivity
import pt.isel.pdm.gomokuroyale.GomokuRoyaleApplication
import pt.isel.pdm.gomokuroyale.TAG
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyActivity
import pt.isel.pdm.gomokuroyale.main.domain.MainScreenState
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingActivity
import pt.isel.pdm.gomokuroyale.ui.ErrorAlert

class MainActivity : ComponentActivity() {

    private val app by lazy { application as GomokuRoyaleApplication }

    private val viewModel by viewModels<MainScreenViewModel> {
        MainScreenViewModel.factory(
            app.userInfoRepository,
            app.gomokuService,
            app.uriRepository,
            app.variantRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate() called")
        lifecycleScope.launch {
            viewModel.state.collect {
                if (it is MainScreenState.Idle) {
                    viewModel.updateRecipes()
                }
                if (it is MainScreenState.FetchedRecipes) {
                    viewModel.updateVariants()
                }
                if (it is MainScreenState.FetchedVariants) {
                    viewModel.fetchPlayerInfo()
                }
            }
        }

        setContent {
            val currentState = viewModel.state.collectAsState(initial = MainScreenState.Idle).value
            val token =
                if (currentState is MainScreenState.FetchedPlayerInfo)
                    currentState.userInfo.getOrNull()?.accessToken
                else
                    null
            MainScreen(
                isLoggedIn = token != null,
                onLoginRequested = { LoginActivity.navigateTo(this) },
                onRegisterRequested = { RegisterActivity.navigateTo(this) },
                onCreateGameRequested = { LobbyActivity.navigateTo(this) },
                onInfoRequested = { AboutActivity.navigateTo(this) },
                onRankingRequested = { RankingActivity.navigateTo(this) },
                onLogoutRequested = { viewModel.logout(token) },
            )

            currentState.let {
                if (it is MainScreenState.FailedToFetch || it is MainScreenState.FailedToLogout) {
                    ErrorAlert(
                        title = "Main Screen Error",
                        message = getErrorMessage(it),
                        buttonText = "Ok",
                        onDismiss = { viewModel.resetToIdle() }
                    )
                }
            }
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

    companion object {
        private fun getErrorMessage(state: MainScreenState): String =
            when (state) {
                is MainScreenState.FailedToFetch -> state.error.message ?: UNKNOWN_ERROR
                is MainScreenState.FailedToLogout -> state.error.message ?: UNKNOWN_ERROR
                else -> UNKNOWN_ERROR
            }

        private const val UNKNOWN_ERROR = "Unknown error"
    }
}

