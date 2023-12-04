package pt.isel.pdm.gomokuroyale.authentication.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginActivity
import pt.isel.pdm.gomokuroyale.util.Loaded
import pt.isel.pdm.gomokuroyale.util.Saved

class RegisterActivity : ComponentActivity() {

    private val userService by lazy {
        (application as DependenciesContainer).gomokuService.userService
    }

    private val viewModel by viewModels<RegisterScreenViewModel> {
        RegisterScreenViewModel.factory(userService)
    }

    companion object {
        fun navigateTo(origin: Activity) {
            val intent = Intent(origin, RegisterActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.collect {
                Log.v("RegisterActivity", "State : $it")
                if (it is Loaded && it.value.isSuccess) {
                    finish()
                    viewModel.resetToIdle()
                }
            }
        }

        setContent {
            RegisterScreen(
                onBackRequested = { finish() },
                onLoginActivity = { LoginActivity.navigateTo(this) },
                onRegisterRequested = { username, email, password ->
                    viewModel.register(username, email, password)
                })
        }
    }
}