package pt.isel.pdm.gomokuroyale.game.lobby.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.TAG
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginActivity
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FailedToFetch
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchVariants
import pt.isel.pdm.gomokuroyale.game.lobby.domain.PlayerInfo
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchmakerActivity
import pt.isel.pdm.gomokuroyale.ui.ErrorAlert

class LobbyActivity : ComponentActivity() {

    private val repo by lazy { (application as DependenciesContainer).userInfoRepository }
    private val variantRepo by lazy { (application as DependenciesContainer).variantRepository }

    private val viewModel by viewModels<LobbyScreenViewModel> {
        LobbyScreenViewModel.factory(repo, variantRepo)
    }

    companion object {
        fun navigateTo(origin: ComponentActivity) {
            with(origin) {
                val intent = Intent(origin, LobbyActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.state.collect {
                if (it is FetchingPlayerInfo) {
                    viewModel.fetchPlayerInfo()
                }
                if (it is FetchedMatchInfo) {
                    MatchmakerActivity.navigateTo(this@LobbyActivity, it.matchInfo)
                    viewModel.resetToFetchingPlayerInfo()
                }
                if (it is FetchedPlayerInfo) {
                    viewModel.fetchVariants()
                }
                if (it is FetchVariants) {
                    Log.v(TAG, "Variants fetched")
                    Log.v(TAG, "Variants: ${it.variants}")
                }
            }
        }

        setContent {
            val state by viewModel.state.collectAsState(initial = FetchingPlayerInfo)
            val userInfo =
                /* if (state !is FetchingPlayerInfo && state !is FailedToFetch)
                     (state as FetchedPlayerInfo).userInfo
                 else
                     null*/
                when (state) {
                    is FetchingPlayerInfo -> null
                    is FetchedPlayerInfo -> (state as FetchedPlayerInfo).userInfo
                    else -> null
                }
            LobbyScreen(
                modifier = if (state is FetchingPlayerInfo) Modifier.shimmer() else Modifier,
                onPlayEnabled = state !is FetchingMatchInfo && state !is FetchedMatchInfo,
                onFindGame = { variant -> viewModel.fetchMatchInfo(variant) },
                playerInfo = if (userInfo != null) PlayerInfo(userInfo.username, 0) else null,
                onNavigationBackRequested = { finish() }
            )

            state.let {
                if (it is FailedToFetch)
                    ErrorAlert(
                        title = "Failed to create lobby",
                        message = it.error.message ?: "Unknown error",
                        buttonText = "Ok",
                        onDismiss = { LoginActivity.navigateTo(this) }
                    )
            }
        }
    }
}