package pt.isel.pdm.gomokuroyale.authentication.ui.login


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.authentication.domain.validatePassword
import pt.isel.pdm.gomokuroyale.authentication.domain.validateUsername
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.components.ButtonComponent
import pt.isel.pdm.gomokuroyale.ui.components.DivideComponent
import pt.isel.pdm.gomokuroyale.ui.components.FieldType
import pt.isel.pdm.gomokuroyale.ui.components.InformationBox
import pt.isel.pdm.gomokuroyale.ui.components.TextComponent
import pt.isel.pdm.gomokuroyale.ui.components.VerificationComponent
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

/*
TODO:
- CHANGE ALL HARDCODED STRINGS AND COLORS TO CONSTANTS AND VALUES
 */




const val LoginScreenTestTag = "LoginScreenTestTag"
private val paddingHead = 30.dp
const val INVALID_USERNAME = "Invalid username. Must be between 5 and 20 characters"
const val INVALID_PASSWORD = "Password must be at least 8 characters and include at least one letter and one number"
const val LOGIN = "Login"
const val NO_HAVE_ACCOUNT = "Don't have an account yet?"
const val HELP_USERNAME ="Between 5 and 20 characters"
const val HELP_PASSWORD = "At least 8 characters and include at least one letter and one number"


@Composable
fun LoginScreen(
    onBackRequested: () -> Unit = { },
    onRegisterRequested: () -> Unit = { },
    onLoginRequested: (username: String, password: String) -> Unit
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LoginScreenTestTag),
            topBar = { TopBar(navigation = NavigationHandlers(onBackRequested = onBackRequested)) },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingHead)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .testTag(LoginScreenTestTag),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    var username by rememberSaveable { mutableStateOf("") }
                    var password by rememberSaveable { mutableStateOf("") }
                    var isUsernameValid by rememberSaveable { mutableStateOf(false) }
                    var isPasswordValid by rememberSaveable { mutableStateOf(false) }


                    var isButtonClicked by rememberSaveable { mutableStateOf(false) }


                    TextComponent(R.string.login_title)
                    InformationBox(
                        label = "Username",
                        value = username,
                        onValueChange = {
                            username = it
                        },
                        resourceId = R.drawable.ic_user,
                        fieldType = FieldType.EMAIL_USER,
                        validateField = validateUsername(username),
                        isError = !isUsernameValid && isButtonClicked,
                        supportText = if (!isUsernameValid && isButtonClicked) INVALID_USERNAME else HELP_USERNAME
                    )
                    InformationBox(
                        label = "Password",
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        resourceId = R.drawable.password,
                        fieldType = FieldType.PASSWORD,
                        validateField = validatePassword(password),
                        isError = !isPasswordValid && isButtonClicked,
                        supportText = if (!isPasswordValid && isButtonClicked) INVALID_PASSWORD else HELP_PASSWORD
                    )
                    ButtonComponent(
                        iconResourceId = R.drawable.ic_enter,
                        text = LOGIN,
                        onClick = {
                            isButtonClicked = true
                            isUsernameValid = validateUsername(username)
                            isPasswordValid = validatePassword(password)
                            if (isUsernameValid && isPasswordValid) {
                                onLoginRequested(username, password)
                            }

                        })
                    DivideComponent()

                    VerificationComponent(
                        text = NO_HAVE_ACCOUNT,
                        textUnderline = "Sign Up",
                        onClick = { onRegisterRequested() })

                }


            }
        }
    }


}


@Preview(showBackground = true)
@Composable
private fun InfoScreenPreview() {

    LoginScreen(
        onLoginRequested = { _, _ -> }
    )
}