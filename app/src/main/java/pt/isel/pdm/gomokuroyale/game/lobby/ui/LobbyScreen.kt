package pt.isel.pdm.gomokuroyale.game.lobby.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variants
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.theme.DarkViolet
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme
import pt.isel.pdm.gomokuroyale.ui.theme.Purple40
import pt.isel.pdm.gomokuroyale.ui.theme.Purple80
import pt.isel.pdm.gomokuroyale.ui.theme.PurpleGrey40
import pt.isel.pdm.gomokuroyale.ui.theme.PurpleGrey80
import pt.isel.pdm.gomokuroyale.ui.theme.Violet

const val LobbyScreenTestTag = "LOBBY_SCREEN_TEST_TAG"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(
    onFindGame: () -> Unit = {},
    onExit: () -> Unit = {}
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LobbyScreenTestTag),
            topBar = { TopBar(NavigationHandlers(onBackRequested = onExit)) },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Violet),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PlayerLobbyInfo()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(DarkViolet),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val radioButtons = remember {
                            mutableStateListOf(*listOfVariants.toTypedArray())
                        }
                        val listSplitter = splitList(radioButtons)
                        Column(horizontalAlignment = Alignment.Start) {
                            ListRadioButtons(listSplitter.first, radioButtons)
                        }
                        Column(horizontalAlignment = Alignment.Start) {
                            ListRadioButtons(listSplitter.second, radioButtons)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.Center) {
                        GradientButton(onFindGame)
                    }
                }
            }
        }
    }
}

@Composable
private fun GradientButton(onFindGame: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp),
        onClick = onFindGame,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(colors = gradientColors),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.play_find_game),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private val gradientColors = listOf(
    Purple80, PurpleGrey40, Purple40, PurpleGrey80
)

@Composable
private fun PlayerLobbyInfo(/* Future Player Info*/) {
    Image(
        modifier = Modifier
            .padding(top = 16.dp)
            .clip(CircleShape)
            .background(Color.White),
        painter = painterResource(id = R.drawable.arqueiro),
        contentDescription = "Player Icon"
    )
    Text(
        text = "Player Name",
        style = MaterialTheme.typography.titleLarge
    )
    Text(text = "Player points")
}

@Composable
private fun ListRadioButtons(
    listSplit: MutableList<ToggleableInfo>,
    radioButtons: SnapshotStateList<ToggleableInfo>,
) {
    listSplit.forEachIndexed { _, info ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    radioButtons.replaceAll {
                        it.copy(
                            isChecked = it.text == info.text
                        )
                    }
                }
                .padding(end = 16.dp)
        ) {
            RadioButton(
                selected = info.isChecked,
                onClick = {
                    radioButtons.replaceAll {
                        it.copy(
                            isChecked = it.text == info.text
                        )
                    }
                }
            )
            Text(text = info.text)
        }
    }
}

private val listOfVariants: List<ToggleableInfo> =
    Variants.entries.map {
        ToggleableInfo(
            isChecked = it == Variants.STANDARD,
            variant = it,
            text = it.name
        )
    }

fun <T> splitList(inputList: MutableList<T>): Pair<MutableList<T>, MutableList<T>> {
    val size = inputList.size
    val middle = size / 2
    val firstHalf = inputList.subList(0, middle + size % 2)
    val secondHalf = inputList.subList(middle + size % 2, size)
    return Pair(firstHalf, secondHalf)
}

private data class ToggleableInfo(
    val isChecked: Boolean,
    val variant: Variants,
    val text: String
)

@Preview(showBackground = true)
@Composable
fun LobbyScreenPreview() {
    LobbyScreen()
}
