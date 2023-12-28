package pt.isel.pdm.gomokuroyale.game.lobby.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginActivity
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FailedToFetch
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedVariants
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingMatchInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingVariants
import pt.isel.pdm.gomokuroyale.game.lobby.domain.Idle
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
                if (it is Idle) {
                    viewModel.fetchPlayerInfo()
                }
                if (it is FetchedPlayerInfo) {
                    viewModel.fetchVariants()
                }
                if (it is FetchedMatchInfo) {
                    MatchmakerActivity.navigateTo(this@LobbyActivity, it.matchInfo)
                    viewModel.resetToIdle()
                }
            }
        }

        setContent {
            val currentState = viewModel.state.collectAsState(initial = FetchingPlayerInfo).value
            val userInfo = when (currentState) {
                is FetchedPlayerInfo -> currentState.userInfo
                is FetchedVariants -> currentState.userInfo
                else -> null
            }
            val modifier =
                if (currentState is FetchingPlayerInfo || currentState is FetchingVariants)
                    Modifier.shimmer()
                else
                    Modifier
            val variants =
                if (currentState is FetchedVariants) currentState.variants
                else emptyList()

            LobbyScreen(
                modifier = modifier,
                variants = variants,
                onPlayEnabled = currentState !is FetchingMatchInfo && currentState !is FetchedMatchInfo,
                onFindGame = { variant ->
                    viewModel.fetchMatchInfo(variants.first { it.name == variant })
                },
                playerInfo = userInfo?.let { PlayerInfo(it.username, 0) },
                onNavigationBackRequested = { finish() }
            )

            currentState.let {
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