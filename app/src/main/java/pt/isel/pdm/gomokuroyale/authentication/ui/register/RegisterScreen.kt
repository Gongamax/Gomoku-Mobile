package pt.isel.pdm.gomokuroyale.authentication.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.authentication.domain.validateConfirmationPassword
import pt.isel.pdm.gomokuroyale.authentication.domain.validateEmail
import pt.isel.pdm.gomokuroyale.authentication.domain.validatePassword
import pt.isel.pdm.gomokuroyale.authentication.domain.validateUsername
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.components.ButtonComponent
import pt.isel.pdm.gomokuroyale.ui.components.DivideComponent
import pt.isel.pdm.gomokuroyale.ui.components.FieldType
import pt.isel.pdm.gomokuroyale.ui.components.IconButtonWithBorder
import pt.isel.pdm.gomokuroyale.ui.components.InformationBox
import pt.isel.pdm.gomokuroyale.ui.components.TextComponent
import pt.isel.pdm.gomokuroyale.ui.components.VerificationComponent
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme


const val RegisterScreenTestTag = "RegisterScreenTestTag"
private val paddingHead = 30.dp
private val errorAcceptTermsSize = 12.sp

const val INVALID_USERNAME = "Invalid username. Must be between 5 and 20 characters"
const val INVALID_PASSWORD =
    "Password must be at least 8 characters and include at least one letter and one number"
const val REGISTER = "Register"
const val HAVE_ACCOUNT = "Already have an account?"
const val INVALID_EMAIL = "Email is not valid. Must have @ and .letter at least"
const val INVALID_CONFIRMATION_PASSWORD = "Password is not valid or not equal to password"
const val INVALID_TERMS = "You must accept the terms"
const val NO_HAVE_ACCOUNT = "Don't have an account yet?"
const val HELP_USERNAME = "Between 5 and 20 characters"
const val HELP_PASSWORD = "At least 8 characters and include at least one letter and one number"
const val HELP_CONFIRMATION_PASSWORD = "Password must be equal to password"
const val HELP_EMAIL = "Email must @ and .letter at least"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackRequested: () -> Unit = { },
    onLoginActivity: () -> Unit = { },
    onRegisterRequested: (username: String, email: String, password: String) -> Unit
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(RegisterScreenTestTag),
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
                        .testTag(RegisterScreenTestTag),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    var username by rememberSaveable { mutableStateOf("") }
                    var email by rememberSaveable { mutableStateOf("") }
                    var password by rememberSaveable { mutableStateOf("") }
                    var passwordConfirmation by rememberSaveable { mutableStateOf("") }
                    var acceptTerms by rememberSaveable { mutableStateOf(false) }
                    var isUsernameValid by rememberSaveable { mutableStateOf(false) }
                    var isEmailValid by rememberSaveable { mutableStateOf(false) }
                    var isPasswordValid by rememberSaveable { mutableStateOf(false) }
                    var isPasswordConfirmationValid by rememberSaveable { mutableStateOf(false) }
                    var isTermsValid by rememberSaveable { mutableStateOf(false) }

                    var isButtonClicked by rememberSaveable { mutableStateOf(false) }

                    TextComponent(R.string.register_title)
                    InformationBox(
                        label = "Username", value = username,
                        onValueChange = {
                            username = it
                        },
                        resourceId = R.drawable.ic_user,
                        fieldType = FieldType.EMAIL_OR_USER,
                        validateField = validateUsername(username),
                        isError = !isUsernameValid && isButtonClicked,
                        supportText = if (!isUsernameValid && isButtonClicked) INVALID_USERNAME else HELP_USERNAME
                    )
                    InformationBox(
                        label = "Email", value = email,
                        onValueChange = {
                            email = it
                        },
                        resourceId = R.drawable.email,
                        fieldType = FieldType.EMAIL_OR_USER,
                        validateField = validateEmail(email),
                        isError = !isEmailValid && isButtonClicked,
                        supportText = if (!isEmailValid && isButtonClicked) INVALID_EMAIL else HELP_EMAIL
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
                        supportText = if (!isPasswordValid && isButtonClicked) pt.isel.pdm.gomokuroyale.authentication.ui.login.INVALID_PASSWORD else pt.isel.pdm.gomokuroyale.authentication.ui.login.HELP_PASSWORD
                    )
                    InformationBox(
                        label = "Confirm Password",
                        value = passwordConfirmation,
                        onValueChange = {
                            passwordConfirmation = it
                        },
                        resourceId = R.drawable.password,
                        fieldType = FieldType.PASSWORD,
                        validateField = validateConfirmationPassword(
                            password,
                            passwordConfirmation
                        ),
                        isError = !isPasswordConfirmationValid && isButtonClicked,
                        supportText = if (!isPasswordConfirmationValid && isButtonClicked) INVALID_CONFIRMATION_PASSWORD else HELP_CONFIRMATION_PASSWORD
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Checkbox(checked = acceptTerms, onCheckedChange =
                        {
                            acceptTerms = it
                        })

                        Text(text = "Accept Terms ")

                        if (!acceptTerms && isButtonClicked) {
                            Text(
                                text = INVALID_TERMS,
                                fontSize = errorAcceptTermsSize,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    ButtonComponent(
                        iconResourceId = R.drawable.ic_enter,
                        text = REGISTER,
                        onClick = {
                            isButtonClicked = true
                            isUsernameValid = validateUsername(username)
                            isEmailValid = validateEmail(email)
                            isPasswordValid = validatePassword(password)
                            isPasswordConfirmationValid = validateConfirmationPassword(
                                password,
                                passwordConfirmation
                            )
                            isTermsValid = acceptTerms

                            if (isUsernameValid && isEmailValid && isPasswordValid && isPasswordConfirmationValid && isTermsValid)
                                onRegisterRequested(username, email, password)
                        })


                    DivideComponent()


                    VerificationComponent(
                        text = HAVE_ACCOUNT,
                        textUnderline = "Login in",
                        onClick = { onLoginActivity() })

                }

            }

        }


    }
}


@Preview(showBackground = true)
@Composable
private fun InfoScreenPreview() {

    RegisterScreen(
        onBackRequested = { },
        onLoginActivity = { },
        onRegisterRequested = { _, _, _ -> })
}
