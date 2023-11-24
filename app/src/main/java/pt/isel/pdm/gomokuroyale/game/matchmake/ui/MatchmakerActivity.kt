package pt.isel.pdm.gomokuroyale.game.matchmake.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyActivity
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.util.getOrThrow

const val MATCHMAKER_ACTIVITY_TAG = "MatchmakerActivity"
const val USER_INFO_EXTRA = "USER_INFO_EXTRA"

class MatchmakerActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel by viewModels<MatchmakerViewModel> {
        MatchmakerViewModel.factory(
            dependencies.gomokuService,
            playerInfo!! // TODO: remove !! (not null assertion)
        )
    }

    companion object {
        fun navigateTo(ctx: Context, info: LobbyActivity.PlayerInfoExtra) {
            ctx.startActivity(createIntent(ctx, info))
        }

        private fun createIntent(ctx: Context, info: LobbyActivity.PlayerInfoExtra): Intent {
            val intent = Intent(ctx, MatchmakerActivity::class.java)
            intent.putExtra(USER_INFO_EXTRA, info)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.match.collect {
//                    if (it is Loaded) {
//                        //TODO
//                    }
//                }
            }
        }

        setContent {
            MatchmakerScreen(viewModel.status.getOrThrow())
        }
    }

    private val playerInfo: PlayerInfo? by lazy { getPlayerInfoExtra()?.toPlayerInfo() }

    @Suppress("DEPRECATION")
    private fun getPlayerInfoExtra(): LobbyActivity.PlayerInfoExtra? =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            intent?.getParcelableExtra(USER_INFO_EXTRA, LobbyActivity.PlayerInfoExtra::class.java)
        else
            intent?.getParcelableExtra(USER_INFO_EXTRA)

    data class PlayerInfo(
        val username: String,
        val token : String,
        val points: Int,
        val variant: Variant
    )

    private fun LobbyActivity.PlayerInfoExtra.toPlayerInfo() = PlayerInfo(
        username = username,
        token = token,
        points = points,
        variant = variant
    )
}