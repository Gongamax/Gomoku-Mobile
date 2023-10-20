package pt.isel.pdm.gomokuroyale.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

const val MainScreenTestTag = "MainScreenTestTag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onCreateGameRequested : () -> Unit = {},
    onInfoRequested: () -> Unit = {},
    onRankingRequested: () -> Unit = {},
    onLoginRequested: () -> Unit = {},
    onRegisterRequested: () -> Unit = {},
    onLogOutRequested: () -> Unit = {},
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(MainScreenTestTag),
            topBar = {
                TopBar(
                    navigation = NavigationHandlers(
                        onInfoRequested = onInfoRequested,
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(vertical = 16.dp),
                    painter = painterResource(id = R.drawable.gomoku),
                    contentDescription = "Gomoku Royale"
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Gomoku Royale",
                        style = MaterialTheme.typography.displayMedium,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.size(30.dp))
                    ButtonMenu(icon = Icons.Default.PlayArrow, text = "Play") {
                        onCreateGameRequested()
                    }
                    ButtonMenu(icon = Icons.Default.List, text = "Ranking") {
                        onRankingRequested()
                    }

                    ButtonMenu(icon = Icons.Default.AccountBox, text = "Register") {
                        onRegisterRequested()
                    }
                    ButtonMenu(icon = Icons.Default.Person, text = "Login") {
                        onLoginRequested()
                    }
//                ButtonMenu(icon = Icons.Default.ExitToApp, text = "Logout") {
//                    onLogOutRequested()
//                }
                }
            }
        }
    }
}

@Composable
private fun ButtonMenu(icon: ImageVector, text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(0.8f),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text, textAlign = TextAlign.Start)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenV2Preview() {
    MainScreen()
}