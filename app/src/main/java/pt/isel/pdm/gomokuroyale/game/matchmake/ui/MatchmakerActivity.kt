package pt.isel.pdm.gomokuroyale.game.matchmake.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.MatchInfo
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.http.domain.MatchmakingStatus
import pt.isel.pdm.gomokuroyale.util.Idle
import pt.isel.pdm.gomokuroyale.util.Loaded
import pt.isel.pdm.gomokuroyale.util.getOrNull
import pt.isel.pdm.gomokuroyale.util.getOrThrow

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
                viewModel.status.collect {
                    if (it is Idle)
                        viewModel.findGame()
                    if (it is Loaded)
                        finish()
                }

            }

        setContent {
            val state = viewModel.status.collectAsState().value
            MatchmakerScreen(
                status = MatchmakingStatus.PENDING,
                onCancelingMatchmaking = viewModel::leaveQueue,
                onCancelingEnabled = true,
                variant = matchInfo?.variant ?: Variant.STANDARD
            )
        }
    }


    private val matchInfo: MatchInfo? by lazy { getPlayerInfoExtra()?.toMatchInfo() }

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
        val variant: Variant
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