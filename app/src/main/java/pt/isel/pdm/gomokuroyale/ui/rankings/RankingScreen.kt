package pt.isel.pdm.gomokuroyale.ui.rankings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.ui.PlayerView
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.about.AboutScreenTestTag
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme
import pt.isel.pdm.gomokuroyale.R

const val MAX_RANKING_NUMBER = 10
data class PlayerInfo(val userName: String, val points: Int)

data class RankingTable(
    val table: List<PlayerInfo>? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(
    onBackRequested: () -> Unit = { },
    onPlayerClicked: (String) -> Unit = { },
    rankingTable: RankingTable
){
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(AboutScreenTestTag),
            topBar = { TopBar(NavigationHandlers(onBackRequested = onBackRequested)) },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Row() {
                    Text(
                        text = "Global LeaderBoard",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        modifier = Modifier
                            .padding(
                                start = 8.dp,
                                top = 8.dp,
                                end = 8.dp,
                                bottom = 16.dp
                            ),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Place",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 35.dp))
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 35.dp))
                    Text(
                        text = "Points",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier
                    )
                }
                if (rankingTable.table != null) {
                    repeat(rankingTable.table.size) {
                        Button(
                            shape = CircleShape,
                            onClick = { onPlayerClicked(rankingTable.table[it].userName) },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            PlayerView(rankingTable.table[it], it)
                        }
                    }
                } else {
                    Text(
                        text = "LeaderBoard",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.background
                    )
                    Text(
                        text = "Loading",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerView(
    playerInfo: PlayerInfo,
    ranking: Int
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        when(val res = ranking + 1 ){
            1 -> IconButton(onClick = { }, enabled = false) {
                Image(painter = painterResource(id = R.drawable.first_place), contentDescription = "1st")
            }

            2 -> IconButton(onClick = { }, enabled = false) {
                Image(painter = painterResource(id = R.drawable.second_place), contentDescription = "2nd")
            }
            3 -> IconButton(onClick = { }, enabled = false) {
                Image(painter = painterResource(id = R.drawable.third_place), contentDescription = "3rd")
            }
            else -> IconButton(onClick = { }, enabled = false) {
                Text(text = "${res}th",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
        }
        Text(
            text = playerInfo.userName,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,

            )
        Text(
            text = "${playerInfo.points}",
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
        )
    }
}

    @Preview(showBackground = true)
    @Composable
    private fun LeaderboardWithoutPlayerPreview() {
        RankingScreen(
            onBackRequested = {},
            onPlayerClicked = {},
            rankingTable = RankingTable(leaderboard)
        )
    }


    @Preview(showBackground = true)
    @Composable
    private fun LeaderboardPreview() {
        RankingScreen(
            onBackRequested = {},
            onPlayerClicked = {},
            rankingTable = RankingTable(null)
        )
    }

    val leaderboard =buildList {
        repeat(MAX_RANKING_NUMBER) {
            add(PlayerInfo( "Player$it", it))
        }
    }.asReversed()
