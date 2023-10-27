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
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.main.TAG
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginScreen


class RegisterActivity : ComponentActivity(){

    companion object {
        fun navigateTo(origin : Activity){
            val intent = Intent(origin, RegisterActivity::class.java)
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