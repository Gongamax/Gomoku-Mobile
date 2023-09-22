package pt.isel.pdm.gomokuroyale

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.isel.pdm.gomokuroyale.ui.AuthorInformationScreen
import pt.isel.pdm.gomokuroyale.ui.BoardView
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GomokuRoyaleTheme {
                CenterObjects()
            }
        }
    }
}

/**
 * Simple temporary function just for testing, keeps the content centered
 */
@Composable
fun CenterObjects() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        BoardView()

        //AuthorInformationScreen()
    }
}
