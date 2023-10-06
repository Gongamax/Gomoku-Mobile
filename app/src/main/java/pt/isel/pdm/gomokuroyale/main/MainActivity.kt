package pt.isel.pdm.gomokuroyale.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.isel.pdm.gomokuroyale.game.play.ui.GomokuViewModel
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GomokuRoyaleTheme {
                val vm = viewModel<GomokuViewModel>(
                    factory = object : ViewModelProvider.Factory {
                        
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return GomokuViewModel() as T
                        }
                    }
                )
                MainScreen(vm)
            }
        }
    }
}

