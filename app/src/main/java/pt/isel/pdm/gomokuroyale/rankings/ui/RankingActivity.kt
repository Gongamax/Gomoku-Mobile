package pt.isel.pdm.gomokuroyale.rankings.ui

import android.app.Activity
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
import pt.isel.pdm.gomokuroyale.matchHistory.ui.MatchHistoryActivity
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState
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
                    vm.resetToIdle()
                }
            }
        }

        setContent {
            val currentState = vm.state.collectAsState(initial = Idle).value
            val players =
                if (currentState is FetchedRankingInfo) currentState.rankingInfo.rankingTable
                else emptyList()
            RankingScreen(
                vmState = currentState,
                isRequestInProgress = currentState is FetchingRankingInfo || currentState is Idle,
                modifier = if (currentState is FetchingRankingInfo) Modifier.shimmer() else Modifier,
                onBackRequested = { finish() },
                players = players,
                onPagedRequested = { page -> vm.getPlayers(page) },
                onMatchHistoryRequested = { id, username -> vm.goToMatchHistory(id, username) },
                onSearchRequested = { username -> vm.search(username.value) },
                onPlayerSelected = { userId -> vm.getUserInfo(userId) },
                onPlayerDismissed = { vm.resetToIdle() },
                isLastPage = currentState is FetchedRankingInfo
                        && currentState.rankingInfo.paginationLinks.last == null
            )
            currentState.let {
                if (it is FailedToFetchRankingInfo || it is FailedToFetchPlayerInfo ||
                    it is FailedToFetchPlayersBySearch
                ) {
                    ErrorAlert(
                        title = "Error",
                        message = it.getErrorMessage(),
                        buttonText = "Ok",
                        onDismiss = { vm.resetToIdle() }
                    )
                }
            }
        }


    }

    private fun RankingScreenState.getErrorMessage(): String {
        return when (this) {
            is FailedToFetchRankingInfo -> error.message ?: UNKNOWN_ERROR
            is FailedToFetchPlayerInfo -> error.message ?: UNKNOWN_ERROR
            is FailedToFetchPlayersBySearch -> error.message ?: UNKNOWN_ERROR
            else -> UNKNOWN_ERROR
        }
    }

    companion object {
        const val UNKNOWN_ERROR = "Unknown error"

        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, RankingActivity::class.java)
            origin.startActivity(intent)
        }
    }
}
