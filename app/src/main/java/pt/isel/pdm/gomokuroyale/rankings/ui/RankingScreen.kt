@file:OptIn(FlowPreview::class)

package pt.isel.pdm.gomokuroyale.rankings.ui

import android.content.SharedPreferences
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.about.ui.AboutScreenTestTag
import pt.isel.pdm.gomokuroyale.ui.components.TextComponent
import pt.isel.pdm.gomokuroyale.ui.theme.DarkViolet

const val MAX_RANKING_NUMBER = 50

data class PlayerInfo(val userName: String, val points: Int)

data class RankingTable(
    val table: List<PlayerInfo>
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RankingScreen(
    onBackRequested: () -> Unit = { },
    onPlayerClicked: (String) -> Unit = { },
    rankingTable: RankingTable,
    prefs: SharedPreferences
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(AboutScreenTestTag),
            topBar = { TopBar(NavigationHandlers(onBackRequested = onBackRequested)) },
        ) { innerPadding ->
            RankingLazyColumn(
                ranks = rankingTable,
                modifier = Modifier.padding(innerPadding),
                onPlayerClicked = onPlayerClicked,
                prefs = prefs
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RankingLazyColumn(
    ranks: RankingTable,
    modifier: Modifier = Modifier,
    onPlayerClicked: (String) -> Unit = { },
    prefs: SharedPreferences
) {
    val scrollPosition = prefs.getInt("scroll_position", 0)
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition
    )

    LaunchedEffect(key1 = lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .debounce(500L)
            .collectLatest { index ->
                prefs.edit()
                    .putInt("scroll_position", index)
                    .apply()
            }
    }

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize(),
    ) {
        stickyHeader {
            RankingHeader()
        }
        items(ranks.table.size) {
            Button(
                shape = CircleShape,
                onClick = { onPlayerClicked(ranks.table[it].userName) },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(1.dp),
                colors = ButtonDefaults.buttonColors(DarkViolet)
            ) {
                PlayerView(ranks.table[it], it)
            }
        }
    }
}

@Composable
fun RankingHeader() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)) {
        Row() {
            TextComponent(R.string.ranking_title, 20.sp, 40.dp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(horizontal = 35.dp))
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
    }
}

@Composable
fun PlayerView(
    playerInfo: PlayerInfo,
    ranking: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        when (val res = ranking + 1) {
            1 -> IconButton(onClick = { }, enabled = false) {
                Image(
                    painter = painterResource(id = R.drawable.first_place),
                    contentDescription = "1st"
                )
            }

            2 -> IconButton(onClick = { }, enabled = false) {
                Image(
                    painter = painterResource(id = R.drawable.second_place),
                    contentDescription = "2nd"
                )
            }

            3 -> IconButton(onClick = { }, enabled = false) {
                Image(
                    painter = painterResource(id = R.drawable.third_place),
                    contentDescription = "3rd"
                )
            }

            else -> IconButton(onClick = { }, enabled = false) {
                Text(
                    text = "${res}th",
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

//    @Preview(showBackground = true)
//    @Composable
//    private fun LeaderboardWithoutPlayerPreview() {
//        RankingScreen(
//            onBackRequested = {},
//            onPlayerClicked = {},
//            rankingTable = RankingTable(leaderboard),
//            prefs = remember {  }
//        )
//    }


val leaderboard = buildList {
    repeat(MAX_RANKING_NUMBER) {
        add(PlayerInfo("Player$it", it * 3))
    }
}.asReversed()
