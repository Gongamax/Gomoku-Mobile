package pt.isel.pdm.gomokuroyale.game.matchmake.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.MatchInfo
import pt.isel.pdm.gomokuroyale.game.matchmake.domain.StartGameInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variants
import pt.isel.pdm.gomokuroyale.game.play.ui.GameActivity
import pt.isel.pdm.gomokuroyale.http.domain.MatchmakingStatus
import pt.isel.pdm.gomokuroyale.ui.ErrorAlert

const val MATCHMAKER_ACTIVITY_TAG = "MATCHMAKER_ACTIVITY_TAG"
const val PLAYER_INFO_EXTRA = "PLAYER_INFO_EXTRA"

class MatchmakerActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel by viewModels<MatchmakerViewModel> {
        MatchmakerViewModel.factory(
            dependencies.gomokuService,
            matchInfo
        )
    }

    companion object {
        fun navigateTo(ctx: Context, info: MatchInfo? = null) {
            Log.v(MATCHMAKER_ACTIVITY_TAG, "Navigating to matchmaker activity.")
            Log.v(MATCHMAKER_ACTIVITY_TAG, "Match info: $info")
            ctx.startActivity(createIntent(ctx, info))
        }

        private fun createIntent(ctx: Context, matchInfo: MatchInfo? = null): Intent {
            val intent = Intent(ctx, MatchmakerActivity::class.java)
            matchInfo?.let { intent.putExtra(PLAYER_INFO_EXTRA, PlayerInfoExtra(it)) }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.screenState.collect {
                if (it is MatchmakingScreenState.Idle) {
                    viewModel.findGame()
                }
                if (it is MatchmakingScreenState.Matched) {
                    GameActivity.navigateTo(
                        this@MatchmakerActivity,
                        StartGameInfo(it.gameId, matchInfo.userInfo)
                    )
                }
                if (it is MatchmakingScreenState.LeftQueue) {
                    finish()
                }
            }

        }

        setContent {
            val currentState = viewModel.screenState.collectAsState().value
            val status = if (currentState is MatchmakingScreenState.Matched)
                MatchmakingStatus.MATCHED
            else
                MatchmakingStatus.PENDING

            MatchmakerScreen(
                status = status,
                onCancelingMatchmaking = viewModel::leaveQueue,
                onCancelingEnabled = currentState !is MatchmakingScreenState.Matched,
                variant = matchInfo.variant
            )

            currentState.let {
                if (it is MatchmakingScreenState.Error) {
                    ErrorAlert(
                        title = "Error",
                        message = it.error.message ?: "Unknown error",
                        buttonText = "Ok",
                        onDismiss = {}
                    )
                }
            }
        }
    }


    private val matchInfo: MatchInfo by lazy {
        checkNotNull(getPlayerInfoExtra()) { "The match info must be provided." }.toMatchInfo()
    }

    @Suppress("DEPRECATION")
    private fun getPlayerInfoExtra(): PlayerInfoExtra? =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent?.getParcelableExtra(PLAYER_INFO_EXTRA, PlayerInfoExtra::class.java)
        else
            intent?.getParcelableExtra(PLAYER_INFO_EXTRA)

    @Parcelize
    data class PlayerInfoExtra(
        val username: String,
        val token: String,
        val points: Int,
        val variant: Variants
    ) : Parcelable {
        constructor(matchInfo: MatchInfo) : this(
            username = matchInfo.userInfo.username,
            token = matchInfo.userInfo.accessToken,
            points = 0,
            variant = matchInfo.variant
        )
    }

    private fun PlayerInfoExtra.toMatchInfo() = MatchInfo(
        userInfo = UserInfo(token, username),
        variant = variant
    )
}