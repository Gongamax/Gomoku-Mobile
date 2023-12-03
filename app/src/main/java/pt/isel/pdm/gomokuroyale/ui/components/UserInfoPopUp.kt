package pt.isel.pdm.gomokuroyale.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pt.isel.pdm.gomokuroyale.http.dto.util.RankingEntry

@Composable
fun UserInfoPopUp(
    onDismissRequest: () -> Unit,
    onMatchHistoryRequested: (Int, String)-> Unit,
    playerInfo: RankingEntry
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card (
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            shape = RectangleShape,
        ){

        }
    }
}