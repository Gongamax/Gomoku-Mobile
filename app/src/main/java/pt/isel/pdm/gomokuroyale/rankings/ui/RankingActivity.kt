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
import pt.isel.pdm.gomokuroyale.rankings.domain.FetchingRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.WantsToGoToMatchHistory

class RankingActivity: ComponentActivity() {

    private val repo by lazy { (application as DependenciesContainer).gomokuService.userService }

    companion object {
        fun navigateTo(origin : Activity){
            val intent = Intent(origin, RankingActivity::class.java)
            origin.startActivity(intent)
        }
    }

    /**
     * The application's dependency provider.
     */
    private val viewModel by viewModels<RankingViewModel> (
        factoryProducer = {
            RankingViewModel.factory(repo)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch{
            viewModel.state.collect {
                if(it is FetchingRankingInfo)
                    viewModel.getPage()
                if (it is WantsToGoToMatchHistory) {
                    MatchHistoryActivity.navigateTo(this@RankingActivity, it.id, it.username)
                    viewModel.resetToFetchingRankingInfo()
                }
            }
        }

        setContent {
            val state by viewModel.state.collectAsState(initial = FetchingRankingInfo)
            RankingScreen(
                vmState = state ,
                modifier = if (state is FetchingRankingInfo) Modifier.shimmer() else Modifier,
                onBackRequested = { finish() },
                onMatchHistoryRequested = { id, username -> viewModel.goToMatchHistory(id, username) },
                onSearchRequested = { username -> viewModel.search(username) },
                onPlayerSelected = { userId -> viewModel.getUserInfo(userId) },
                onPlayerDismissed = { viewModel.resetToFetchingRankingInfo() }
            )
        }
    }
}
