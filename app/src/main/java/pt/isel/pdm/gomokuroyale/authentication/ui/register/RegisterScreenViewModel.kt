package pt.isel.pdm.gomokuroyale.authentication.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.authentication.domain.User
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.http.services.users.UserService
import pt.isel.pdm.gomokuroyale.util.IOState
import pt.isel.pdm.gomokuroyale.util.Idle
import pt.isel.pdm.gomokuroyale.util.Loading
import pt.isel.pdm.gomokuroyale.util.idle
import pt.isel.pdm.gomokuroyale.util.loadFailure
import pt.isel.pdm.gomokuroyale.util.loaded
import pt.isel.pdm.gomokuroyale.util.loading

class RegisterScreenViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _state = MutableStateFlow<IOState<User?>>(idle())

    val state: Flow<IOState<User?>> get() = _state.asStateFlow()

    fun register(username: String, email: String, password: String) {
        if (_state.value !is Idle)
            throw IllegalStateException("Cannot register while loading")
        _state.value = loading()
        viewModelScope.launch {
            val response = userService.register(username, email, password)
            Log.v("RegisterScreenViewModel", "Response: $response")
            _state.value = loadFailure(Exception("UNKNOWN"))
            val result = kotlin.runCatching { User(username, email, password) }
            _state.value = loaded(result)
        }
    }

    fun resetToIdle() {
        if (_state.value is Loading)
            throw IllegalStateException("Cannot reset while loading")
        _state.value = idle()
    }

    companion object {
        fun factory(userService: UserService) = viewModelFactory {
            initializer { RegisterScreenViewModel(userService) }
        }
    }
}