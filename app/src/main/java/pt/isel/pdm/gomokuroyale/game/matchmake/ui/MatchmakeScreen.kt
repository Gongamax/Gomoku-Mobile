package pt.isel.pdm.gomokuroyale.game.matchmake.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.gomokuroyale.http.dto.util.MatchmakingStatus
import pt.isel.pdm.gomokuroyale.ui.RefreshFab
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

@Composable
fun MatchmakerScreen(status : MatchmakingStatus) {
    GomokuRoyaleTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (status) {
                MatchmakingStatus.PENDING -> RefreshFab(onClick = { /* TODO */ })
                MatchmakingStatus.MATCHED -> Text(text = "Matched!")
            }
        }
    }
}

@Preview
@Composable
fun MatchmakerScreenPreview() {
    MatchmakerScreen(MatchmakingStatus.PENDING)
}

@Preview
@Composable
fun MatchmakerScreenMatchedPreview() {
    MatchmakerScreen(MatchmakingStatus.MATCHED)
}