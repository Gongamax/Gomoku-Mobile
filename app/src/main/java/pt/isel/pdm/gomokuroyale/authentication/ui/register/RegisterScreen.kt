package pt.isel.pdm.gomokuroyale.authentication.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.components.ButtonComponent
import pt.isel.pdm.gomokuroyale.ui.components.DivideComponent
import pt.isel.pdm.gomokuroyale.ui.components.IconButtonWithBorder
import pt.isel.pdm.gomokuroyale.ui.components.InformationBox
import pt.isel.pdm.gomokuroyale.ui.components.TextComponent
import pt.isel.pdm.gomokuroyale.ui.components.VerificationComponent
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme


const val RegisterScreenTestTag = "RegisterScreenTestTag"
private val paddingHead = 30.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBackRequested: () -> Unit = { }) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(RegisterScreenTestTag),
            topBar = { TopBar(NavigationHandlers(onBackRequested = onBackRequested)) },
        ) { innerPadding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingHead)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    TextComponent(R.string.register_title)
                    InformationBox(
                        text = "ID Name",
                        value = "",
                        onValueChange = {},
                        resourceId = R.drawable.ic_user
                    )
                    InformationBox(text = "Email", value = "",onValueChange = {}, resourceId = R.drawable.email)
                    InformationBox(text = "Password", value = "",onValueChange = {}, resourceId = R.drawable.password)
                    InformationBox(text = "Confirm Password ", value = "", onValueChange = {},resourceId = R.drawable.password)

                    ButtonComponent(
                        iconResourceId = R.drawable.ic_enter,
                        text = "Register",
                        onClick = {})
                    DivideComponent()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButtonWithBorder(
                            iconResourceId = R.drawable.google,
                            onClick = {})
                        IconButtonWithBorder(
                            iconResourceId = R.drawable.facebook,
                            onClick = {})
                    }

                    VerificationComponent(
                        text = "Already have an account?",
                        textUnderline = "Login in",
                        onClick = {})

                }


            }

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun InfoScreenPreview() {

    RegisterScreen()
}
