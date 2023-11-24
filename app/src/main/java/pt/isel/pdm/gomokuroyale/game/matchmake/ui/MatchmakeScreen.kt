package pt.isel.pdm.gomokuroyale.game.matchmake.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.http.dto.util.MatchmakingStatus
import pt.isel.pdm.gomokuroyale.ui.TipInfoBox
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

@Composable
fun MatchmakerScreen(
    status: MatchmakingStatus,
    modifier: Modifier = Modifier,
    variant: Variant,
    onCancelingEnabled: Boolean = true,
    onCancelingMatchmaking: () -> Unit = {},
) {
    GomokuRoyaleTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when (status) {
                MatchmakingStatus.PENDING -> {
                    Text(text = "Matchmaking...", style = MaterialTheme.typography.titleLarge)
                    Image(
                        painter = painterResource(id = R.drawable.matchmaking),
                        contentDescription = "matchmaking gif"
                    )
                }

                MatchmakingStatus.MATCHED -> {
                    Text(text = "Matched!", style = MaterialTheme.typography.titleLarge)
                }

                MatchmakingStatus.LEFT_QUEUE -> {
                    Text(
                        text = "Canceling Matchmaking...",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            if (status == MatchmakingStatus.PENDING)
                Button(onClick = onCancelingMatchmaking, enabled = onCancelingEnabled) {
                    Text(text = "Cancel")
                }
            TipInfoBox(tipText = variant.tip)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchmakerScreenPreview() {
    MatchmakerScreen(MatchmakingStatus.PENDING, variant = Variant.STANDARD)
}

@Preview(showBackground = true)
@Composable
fun MatchmakerScreenMatchedPreview() {
    MatchmakerScreen(MatchmakingStatus.MATCHED, variant = Variant.STANDARD)
}