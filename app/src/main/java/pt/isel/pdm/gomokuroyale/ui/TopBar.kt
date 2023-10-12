package pt.isel.pdm.gomokuroyale.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

data class NavigationHandlers(
    val onBackRequested: (() -> Unit)? = null,
    val onInfoRequested: (() -> Unit)? = null,
)

// Test tags for the TopBar navigation elements
const val NavigateBackTestTag = "NavigateBack"
const val NavigateToInfoTestTag = "NavigateToInfo"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigation: NavigationHandlers = NavigationHandlers()) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            if (navigation.onBackRequested != null) {
                IconButton(
                    onClick = navigation.onBackRequested,
                    modifier = Modifier.testTag(NavigateBackTestTag)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.top_bar_go_back)
                    )
                }
            }
        },
        actions = {
            if (navigation.onInfoRequested != null) {
                IconButton(
                    onClick = navigation.onInfoRequested,
                    modifier = Modifier.testTag(NavigateToInfoTestTag)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(id = R.string.top_bar_navigate_to_about)
                    )
                }
            }
        }
    )}

@Preview
@Composable
private fun TopBarPreviewInfoAndHistory() {
    GomokuRoyaleTheme {
        TopBar(
            navigation = NavigationHandlers(onInfoRequested = { })
        )
    }
}

@Preview
@Composable
private fun TopBarPreviewBackAndInfo() {
    GomokuRoyaleTheme {
        TopBar(navigation = NavigationHandlers(onBackRequested = { }, onInfoRequested = { }))
    }
}

@Preview
@Composable
private fun TopBarPreviewBack() {
    GomokuRoyaleTheme {
        TopBar(navigation = NavigationHandlers(onBackRequested = { }))
    }
}