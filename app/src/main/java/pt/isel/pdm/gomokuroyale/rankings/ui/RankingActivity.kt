package pt.isel.pdm.gomokuroyale.rankings.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.matchHistory.ui.MatchHistoryActivity
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FailedToFetchPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FailedToFetchPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FailedToFetchRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchedRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchingRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.WantsToGoToMatchHistory
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.Idle
import pt.isel.pdm.gomokuroyale.ui.ErrorAlert

const val RANKING_ACTIVITY_TAG = "RANKING_ACTIVITY_TAG"

class RankingActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, RankingActivity::class.java)
            origin.startActivity(intent)
        }
    }

    private val vm by viewModels<RankingScreenViewModel> {
        RankingScreenViewModel.factory(
            dependencies.gomokuService.userService,
            dependencies.userInfoRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            vm.state.collect {
                if (it is Idle) {
                    vm.getPlayers()
                }
                if (it is WantsToGoToMatchHistory) {
                    MatchHistoryActivity.navigateTo(this@RankingActivity, it.id, it.username)
                    vm.resetToFetchingRankingInfo()
                }
            }
        }

        setContent {
            val state = vm.state.collectAsState(initial = Idle).value
            val players = if (state is FetchedRankingInfo) state.rankingInfo.rank else emptyList()
            Log.v(RANKING_ACTIVITY_TAG, "state: $state")
            val currentPage = (state as? FetchedRankingInfo)?.page ?: 1
            Log.v(RANKING_ACTIVITY_TAG, "currentPage: $currentPage")
            RankingScreen(
                vmState = state,
                isRequestInProgress = state is FetchingRankingInfo || state is Idle,
                modifier = if (state is FetchingRankingInfo) Modifier.shimmer() else Modifier,
                onBackRequested = { finish() },
                players = players,
                onPagedRequested = { page -> vm.getPlayers(page) },
                onMatchHistoryRequested = { id, username -> vm.goToMatchHistory(id, username) },
                onSearchRequested = { username -> vm.search(username) },
                onPlayerSelected = { userId -> vm.getUserInfo(userId) },
                onPlayerDismissed = { vm.resetToFetchingRankingInfo() },
                currentPage = currentPage
            )

            state.let {
                if (it is FailedToFetchRankingInfo || it is FailedToFetchPlayerInfo ||
                    it is FailedToFetchPlayersBySearch
                )
                    ErrorAlert(
                        title = "Error",
                        message = "Failed to fetch ranking info",
                        buttonText = "Ok",
                        onDismiss = { vm.resetToFetchingRankingInfo() }
                    )
            }
        }
    }
}
