package pt.isel.pdm.gomokuroyale.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pt.isel.pdm.gomokuroyale.http.domain.users.UserRanking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoPopUp(
    onDismissRequest: () -> Unit,
    onMatchHistoryRequested: (Int, String) -> Unit,
    playerInfo: UserRanking
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            shape = RectangleShape,
            onClick = { onMatchHistoryRequested(playerInfo.id, playerInfo.username) }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Username: ${playerInfo.username}")
                Text(text = "Rank: ${playerInfo.rank}")
                Text(text = "Score: ${playerInfo.points}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoPopUpPreview() {
    UserInfoPopUp(
        onDismissRequest = {},
        onMatchHistoryRequested = { _, _ -> },
        playerInfo = UserRanking(
            id = 1,
            username = "username",
            rank = 1,
            points = 1000,
            wins = 1,
            losses = 1,
            gamesPlayed = 2,
        )
    )
}