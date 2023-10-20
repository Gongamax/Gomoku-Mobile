package pt.isel.pdm.gomokuroyale.game.play.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.game.play.domain.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.BoardRun
import pt.isel.pdm.gomokuroyale.game.play.domain.Cell
import pt.isel.pdm.gomokuroyale.game.play.domain.Player
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme


const val GameScreenTestTag = "GameScreenTestTag"

const val BUTTON_COLOR = 0xFF7E91DB

@Composable
fun ButtonComponent(iconResourceId: Int, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color(BUTTON_COLOR))
    ) {
        Icon(
            painter = painterResource(id = iconResourceId),
            contentDescription = "",
            modifier = Modifier
                .height(25.dp)
                .padding(4.dp),
            tint = Color.Black
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = text,
            textAlign = TextAlign.Start,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    onBackRequested: () -> Unit = { }, board: Board?,
    onClick: (Cell) -> Unit = {}
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(GameScreenTestTag),
            topBar = { TopBar(NavigationHandlers(onBackRequested = onBackRequested)) },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0xFF96D7BB)
//                        brush = Brush.linearGradient(
//                            colors = listOf(
//                                Color(0xFF96D7BB),
//                                Color(0xFF75BFBC),
//                                Color(0xFF89CCCA)
//                            ),
//                            start = Offset.Zero,
//                            end = Offset.Infinite,
//                            tileMode = TileMode.Clamp
//                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Player WHITE : 0", fontSize = 16.sp)
                        Text("Player BLACK : 0", fontSize = 16.sp)
                    }
                    BoardView(BoardRun(emptyMap(), Player.BLACK), onClick = {})

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ButtonComponent(
                            iconResourceId = R.drawable.ic_exit,
                            text = "Exit",
                            onClick = {})
                        ButtonComponent(
                            iconResourceId = R.drawable.ic_robot,
                            text = "Help",
                            onClick = {})
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    GameScreen({},BoardRun(emptyMap(), Player.BLACK), onClick = {})
}