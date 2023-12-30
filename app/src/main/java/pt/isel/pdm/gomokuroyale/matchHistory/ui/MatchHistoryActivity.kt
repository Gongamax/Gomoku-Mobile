package pt.isel.pdm.gomokuroyale.matchHistory.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.FetchingVariants
import pt.isel.pdm.gomokuroyale.matchHistory.domain.FetchedMatchHistory
import pt.isel.pdm.gomokuroyale.matchHistory.domain.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.matchHistory.domain.Idle
import pt.isel.pdm.gomokuroyale.matchHistory.domain.MatchHistoryViewModel

class MatchHistoryActivity: ComponentActivity() {

    private val repo by lazy { (application as DependenciesContainer).userInfoRepository }
    private val service by lazy { (application as DependenciesContainer).gomokuService.gameService }

    private val viewModel by viewModels<MatchHistoryViewModel> {
        MatchHistoryViewModel.factory(repo, service)
    }


    companion object {
        fun navigateTo(origin: Activity, playerId: Int, username: String) {
            with(origin) {
                val intent = Intent(origin, MatchHistoryActivity::class.java)
                intent.putExtra(USERS_EXTRA, UsersExtra(playerId, username))
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = getUserExtra()

        lifecycleScope.launch {
            viewModel.state.collect {
                if (it is Idle) {
                    viewModel.fetchPlayerInfo()
                }
                if (it is FetchedPlayerInfo) {
                    viewModel.fetchMatchHistory(extras?.playerId ?: -1)
                }
                if (it is FetchedMatchHistory) {
                    viewModel.resetToIdle()
                }
            }
        }

        setContent {
            val currentState = viewModel.state.collectAsState(initial = FetchingPlayerInfo).value
            val modifier =
                if (currentState is FetchingPlayerInfo || currentState is FetchingVariants)
                    Modifier.shimmer()
                else
                    Modifier
            val matches =
                if (currentState is FetchedMatchHistory) currentState.matchHistory
                else emptyList()

            extras?.let { userExtra ->
                MatchHistoryScreen(
                    modifier = modifier,
                    onBackRequested = { finish() },
                    username = userExtra.username,
                    matches = matches
                )
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getUserExtra(): UsersExtra? =
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(USERS_EXTRA, UsersExtra::class.java)
        else
            intent.getParcelableExtra(USERS_EXTRA)

}

private const val USERS_EXTRA = "UserStatsActivity.extra.users"

@Parcelize
data class UsersExtra(val playerId: Int, val username: String) : Parcelable