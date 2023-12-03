package pt.isel.pdm.gomokuroyale.authentication.ui.register

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.DependenciesContainer
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginActivity
import pt.isel.pdm.gomokuroyale.main.TAG
import pt.isel.pdm.gomokuroyale.util.Saved

//import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginScreen


class RegisterActivity : ComponentActivity() {

    private val dependencies by lazy { (application as DependenciesContainer) }

    private val viewModel by viewModels<RegisterScreenViewModel> {
        RegisterScreenViewModel.factory(
            dependencies.userInfoRepository,
            dependencies.gomokuService.userService
        )
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
                if (it is Saved && it.value.isSuccess) {
                    finish()
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