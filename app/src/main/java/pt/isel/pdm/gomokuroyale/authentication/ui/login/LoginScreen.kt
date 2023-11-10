package pt.isel.pdm.gomokuroyale.authentication.ui.login


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import pt.isel.pdm.gomokuroyale.ui.components.TextComponent1
import pt.isel.pdm.gomokuroyale.ui.components.VerificationComponent
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

const val BACKGROUND = 0xFFFF4FC3F7
const val TEXT_BOX = 0xFFBDBDBD

const val LoginScreenTestTag = "LoginScreenTestTag"
private val paddingHead = 30.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onBackRequested: () -> Unit = { }) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LoginScreenTestTag),
            topBar = { TopBar(NavigationHandlers(onBackRequested = onBackRequested)) },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingHead)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    TextComponent( "Login")
                    InformationBox(
                        text = "Enter Email...",
                        value = "",
                        onValueChange = {},
                        resourceId = R.drawable.email
                    )
                    InformationBox(
                        text = "Password",
                        value = "",
                        onValueChange = {},
                        resourceId = R.drawable.password
                    )
                    VerificationComponent(textUnderline = "Forgot Password?", onClick = {})
                    ButtonComponent(
                        iconResourceId = R.drawable.ic_enter,
                        text = "Login",
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
                        text = "Don't have an account yet?",
                        textUnderline = "Sign Up",
                        onClick = {})

                }


            }

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun InfoScreenPreview() {

    LoginScreen()
}