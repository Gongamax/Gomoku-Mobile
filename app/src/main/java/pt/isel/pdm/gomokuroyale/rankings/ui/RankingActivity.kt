package pt.isel.pdm.gomokuroyale.rankings.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.matchHistory.ui.MatchHistoryActivity
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FailedToFetch
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchedPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchedRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchingPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.FetchingRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.Idle
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenState.WantsToGoToMatchHistory
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
                    //vm.resetToIdle()
                }
            }
        }
        setContent {
            val currentState = vm.state.collectAsState(initial = Idle).value
            val players = currentState.rankingList
            RankingScreen(
                isRequestInProgress = currentState.isRequestInProgress,
                onBackRequested = { finish() },
                players = players,
                onPagedRequested = { page -> vm.getPlayers(page) },
                onMatchHistoryRequested = { id, username -> vm.goToMatchHistory(id, username) },
                onSearchRequested = { username -> vm.search(username.value) },
                onPlayerSelected = { userId -> vm.getUserInfo(userId) },
                isPlayerFetched = currentState is FetchedPlayerInfo,
                playerInfo = (currentState as? FetchedPlayerInfo)?.playerInfo,
                onPlayerDismissed = { /*vm.resetToIdle()*/ },
                isLastPage = currentState.isLastPage
            )
            currentState.let {
                if (it is FailedToFetch)
                    ErrorAlert(
                        title = "Error",
                        message = it.error.message ?: UNKNOWN_ERROR,
                        buttonText = "Ok",
                        onDismiss = { vm.resetToIdle() }
                    )
            }
        }
    }

    private val RankingScreenState.isRequestInProgress
        get() =
            this is FetchingRankingInfo || this is FetchingPlayersBySearch || this is Idle

    private val RankingScreenState.isLastPage
        get() =
            this is FetchedRankingInfo && this.rankingInfo.paginationLinks.last == null

    private val RankingScreenState.rankingList
        get() = when (this) {
            is FetchedRankingInfo -> this.rankingInfo.rankingTable
            is FetchedPlayersBySearch -> this.players.rankingTable
            else -> emptyList()
        }

    companion object {
        const val UNKNOWN_ERROR = "Unknown error"

        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, RankingActivity::class.java)
            origin.startActivity(intent)
        }
    }
}
