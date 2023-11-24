package pt.isel.pdm.gomokuroyale.authentication.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.gomokuroyale.DependenciesContainer

class LoginActivity : ComponentActivity(){

    // Missing logic to put in practice the saving of the user's credentials
    private val repo by lazy {
        (application as DependenciesContainer).userInfoRepository
    }

    companion object {
        fun navigateTo(origin : Activity){
            val intent = Intent(origin, LoginActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            LoginScreen(onBackRequested = { finish() })
        }
    }
}