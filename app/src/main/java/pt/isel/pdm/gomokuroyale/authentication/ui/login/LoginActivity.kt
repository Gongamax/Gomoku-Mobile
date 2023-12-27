package pt.isel.pdm.gomokuroyale.authentication.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.authentication.domain.validateUsername
import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterActivity
import pt.isel.pdm.gomokuroyale.ui.ErrorAlert
//import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterActivity
import pt.isel.pdm.gomokuroyale.util.Idle
import pt.isel.pdm.gomokuroyale.util.Loaded
import pt.isel.pdm.gomokuroyale.util.Loading
import pt.isel.pdm.gomokuroyale.util.Saved
import pt.isel.pdm.gomokuroyale.util.idle

class LoginActivity : ComponentActivity() {

    private val dependencies by lazy { (application as DependenciesContainer) }

    private val viewModel by viewModels<LoginScreenViewModel> {
        LoginScreenViewModel.factory(
            dependencies.userInfoRepository,
            dependencies.gomokuService.userService
        )
    }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, LoginActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.collect {
                if (it is Saved && it.value.isSuccess) {
                    finish()
                    viewModel.resetToIdle()
                }
            }
        }
        setContent {
            val currentState = viewModel.state.collectAsState(initial = idle()).value
            LoginScreen(
                onBackRequested = { finish() },
                onRegisterRequested = { RegisterActivity.navigateTo(this) },
                onLoginRequested = { username, password ->
                    viewModel.login(username, password)
                })

            currentState.let {
                if (it is Saved && it.value.isFailure)
                    ErrorAlert(
                        title = "Login failed",
                        message = it.value.exceptionOrNull()?.message ?: "Unknown error",
                        buttonText = "Ok",
                        onDismiss = { viewModel.resetToIdle() }
                    )
            }
        }
    }


}