package pt.isel.pdm.gomokuroyale.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Simple Author Screen, to be improved next week
 */
@Composable
fun AuthorInformationScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Authors",
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Author 1: Daniel Carvalho ",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Author 2: Francisco Saraiva",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Author 3: Gonçalo Frutuoso",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
