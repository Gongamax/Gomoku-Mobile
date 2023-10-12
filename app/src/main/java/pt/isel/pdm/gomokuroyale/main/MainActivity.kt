package pt.isel.pdm.gomokuroyale.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.isel.pdm.gomokuroyale.game.play.ui.GomokuViewModel
import pt.isel.pdm.gomokuroyale.ui.about.AboutActivity
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

const val TAG = "GOMOKU_ROYALE_TAG"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate() called")
        setContent {
            GomokuRoyaleTheme {
                val vm = viewModel<GomokuViewModel>(
                    factory = object : ViewModelProvider.Factory {
                        
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return GomokuViewModel() as T
                        }
                    }
                )
                MainScreen(vm, onInfoRequested = { AboutActivity.navigateTo(this) })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy() called")
    }
}

