package pt.isel.pdm.gomokuroyale.authentication.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import pt.isel.pdm.gomokuroyale.authentication.ui.components.ButtonComponent
import pt.isel.pdm.gomokuroyale.authentication.ui.components.DivideComponent
import pt.isel.pdm.gomokuroyale.authentication.ui.components.IconButtonWithBorder
import pt.isel.pdm.gomokuroyale.authentication.ui.components.InformationBox
import pt.isel.pdm.gomokuroyale.authentication.ui.components.TextComponent
import pt.isel.pdm.gomokuroyale.authentication.ui.components.VerificationComponent
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme


const val RegisterScreenTestTag = "RegisterScreenTestTag"

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
                .padding(35.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    TextComponent("Register")
                    Spacer(modifier = Modifier.height(50.dp))
                    InformationBox(
                        text = "ID Name",
                        value = "",
                        onValueChange = {},
                        resourceId = R.drawable.ic_user
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    InformationBox(text = "Email", value = "",onValueChange = {}, resourceId = R.drawable.email)
                    Spacer(modifier = Modifier.height(10.dp))
                    InformationBox(text = "Password", value = "",onValueChange = {}, resourceId = R.drawable.password)
                    Spacer(modifier = Modifier.height(10.dp))
                    InformationBox(text = "Confirm Password ", value = "", onValueChange = {},resourceId = R.drawable.password)
                    Spacer(modifier = Modifier.height(20.dp))
                    ButtonComponent(
                        iconResourceId = R.drawable.ic_enter,
                        text = "Register",
                        onClick = {})
                    Spacer(modifier = Modifier.height(20.dp))
                    DivideComponent()
                    Spacer(modifier = Modifier.height(20.dp))
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

                    Spacer(modifier = Modifier.height(40.dp))

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
