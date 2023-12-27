package pt.isel.pdm.gomokuroyale.rankings.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import pt.isel.pdm.gomokuroyale.matchHistory.ui.MatchHistoryActivity
import pt.isel.pdm.gomokuroyale.rankings.domain.FailedToFetchPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.FailedToFetchPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.domain.FailedToFetchRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.FetchedRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.FetchingRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.WantsToGoToMatchHistory
import pt.isel.pdm.gomokuroyale.ui.ErrorAlert

class RankingActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, RankingActivity::class.java)
            origin.startActivity(intent)
        }
    }

    private val viewModel by viewModels<RankingScreenViewModel> {
        RankingScreenViewModel.factory(
            dependencies.gomokuService.userService,
            dependencies.userInfoRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.state.collect {
                if (it is FetchingRankingInfo)
                    viewModel.getPlayers()
                if (it is WantsToGoToMatchHistory) {
                    MatchHistoryActivity.navigateTo(this@RankingActivity, it.id, it.username)
                    viewModel.resetToFetchingRankingInfo()
                }
            }
        }

        setContent {
            val state by viewModel.state.collectAsState(initial = FetchingRankingInfo)
            val players =
                if (state is FetchedRankingInfo)
                    (state as FetchedRankingInfo).rankingInfo.rank
                else
                    listOf()
            RankingScreen(
                vmState = state,
                modifier = if (state is FetchingRankingInfo) Modifier.shimmer() else Modifier,
                onBackRequested = { finish() },
                players = players,
                onPagedRequested = { page -> viewModel.getPlayers(page) },
                onMatchHistoryRequested = { id, username ->
                    viewModel.goToMatchHistory(
                        id,
                        username
                    )
                },
                onSearchRequested = { username -> viewModel.search(username) },
                onPlayerSelected = { userId -> viewModel.getUserInfo(userId) },
                onPlayerDismissed = { viewModel.resetToFetchingRankingInfo() }
            )

            state.let {
                if (it is FailedToFetchRankingInfo || it is FailedToFetchPlayerInfo ||
                    it is FailedToFetchPlayersBySearch
                )
                    ErrorAlert(
                        title = "Error",
                        message = "Failed to fetch ranking info",
                        buttonText = "Ok",
                        onDismiss = { viewModel.resetToFetchingRankingInfo() }
                    )
            }
        }
    }
}
