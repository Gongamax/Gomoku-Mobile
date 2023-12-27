package pt.isel.pdm.gomokuroyale.rankings.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.about.ui.AboutScreenTestTag
import pt.isel.pdm.gomokuroyale.http.domain.users.UserRanking
import pt.isel.pdm.gomokuroyale.rankings.domain.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.FetchingRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState
import pt.isel.pdm.gomokuroyale.rankings.domain.unitsConverter
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.components.LoadingView
import pt.isel.pdm.gomokuroyale.ui.components.MyIcon
import pt.isel.pdm.gomokuroyale.ui.components.MySearchBar
import pt.isel.pdm.gomokuroyale.ui.components.UserInfoPopUp
import pt.isel.pdm.gomokuroyale.ui.theme.DarkViolet
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

const val SearchBarTestTag = "SEARCH_BAR_TEST_TAG"

@Composable
fun RankingScreen(
    vmState: RankingScreenState,
    modifier: Modifier = Modifier,
    onBackRequested: () -> Unit = { },
    players: List<UserRanking> = listOf(),
    onPagedRequested: (Int) -> Unit = { },
    onMatchHistoryRequested: (Int, String) -> Unit = { _, _ -> },
    onSearchRequested: (String) -> Unit = { },
    onPlayerSelected: (Int) -> Unit = { },
    onPlayerDismissed: () -> Unit = { },
) {
    GomokuRoyaleTheme {
        var query by remember { mutableStateOf("") }
        val listState = rememberLazyListState()
        var page by remember { mutableIntStateOf(0) }
        //val players by remember { mutableStateOf((vmState as FetchedRankingInfo).rankingInfo.rank) }
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .testTag(AboutScreenTestTag),
            topBar = {
                TopBar(
                    title = {
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = stringResource(id = R.string.ranking_title),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigation = NavigationHandlers(onBackRequested = onBackRequested)
                )
            },
        ) { innerPadding ->
            RankingLazyColumn(
                query = query,
                listState = listState,
                rank = players,
                state = vmState,
                modifier = modifier.padding(innerPadding),
                onMatchHistoryRequested = onMatchHistoryRequested,
                onSearchRequested = { onSearchRequested(it) },
                onQueryChanged = { query = it },
                onClearSearch = { query = "" },
                onPlayerSelected = { onPlayerSelected(it) },
                onPlayerDismissed = onPlayerDismissed
            )
        }
    }
}
//            LaunchedEffect(key1 = page) {
//                if (page == 0) {
//                    listState.scrollToItem(0)
//                } else {
//                    onPagedRequested(page)
//                }
//            }
//            LaunchedEffect(key1 = listState) {
//                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
//                    .collectLatest { itemIndex ->
//                        if (vmState is FetchingRankingInfo && itemIndex != null && itemIndex >= players.size - 1)
//                            page++
//                    }
//            }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RankingLazyColumn(
    query: String,
    listState: LazyListState,
    state: RankingScreenState,
    rank: List<UserRanking>,
    modifier: Modifier = Modifier,
    onMatchHistoryRequested: (Int, String) -> Unit = { _, _ -> },
    onSearchRequested: (String) -> Unit = { },
    onQueryChanged: (String) -> Unit = { },
    onClearSearch: () -> Unit = { },
    onPlayerSelected: (Int) -> Unit = { },
    onPlayerDismissed: () -> Unit = { },
) {
    LazyColumn(
        userScrollEnabled = true,
        state = listState,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        stickyHeader {
            MySearchBar(query, onSearchRequested, onQueryChanged, onClearSearch)
            Spacer(modifier = Modifier.padding(5.dp))
        }
        rank.forEach { player ->
            item {
                PlayerView(player, onPlayerSelected)
                Spacer(modifier = Modifier.padding(1.dp))
            }
        }
        if (state is FetchingRankingInfo) {
            item {
                LoadingView()
            }
        }
        if (state is FetchedPlayerInfo) {
            item {
                UserInfoPopUp(
                    onDismissRequest = { },
                    onMatchHistoryRequested = onMatchHistoryRequested,
                    playerInfo = state.playerInfo,
                )
            }
        }
    }
}

@Composable
fun PlayerView(
    playerInfo: UserRanking,
    onPlayerSelected: (Int) -> Unit = { },
) {
    Card(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onPlayerSelected(playerInfo.id)
            },
        colors = CardDefaults.cardColors(containerColor = DarkViolet)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            when (val res = playerInfo.rank) {
                in top3.keys -> top3[res]?.invoke()
                else -> IconButton(onClick = { }, enabled = false) {
                    Text(
                        text = "${res}.",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = playerInfo.username,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.padding(15.dp))
            Text(
                text = playerInfo.points.unitsConverter(),
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
            MyIcon(resultId = R.drawable.ic_coins, size = 40.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LeaderboardPreview() {
    RankingScreen(
        vmState = FetchingRankingInfo,
        onBackRequested = {},
        onMatchHistoryRequested = { _, _ -> },
    )
}

private val top3 = mapOf<Int, @Composable () -> Unit>(
    1 to { MyIcon(resultId = R.drawable.first_place) },
    2 to { MyIcon(resultId = R.drawable.second_place) },
    3 to { MyIcon(resultId = R.drawable.third_place) }
)