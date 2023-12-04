package pt.isel.pdm.gomokuroyale.authentication.ui.register

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
import pt.isel.pdm.gomokuroyale.util.idle
import pt.isel.pdm.gomokuroyale.util.loaded
import pt.isel.pdm.gomokuroyale.util.loading

class RegisterScreenViewModel(
    private val userInfoRepository: UserInfoRepository,
    private val userService: UserService
) : ViewModel() {


    companion object {
        fun factory(
            userInfoRepository: UserInfoRepository,
            userService: UserService
        ) = viewModelFactory {
            initializer { RegisterScreenViewModel(userInfoRepository, userService) }
        }
    }

    private val _state = MutableStateFlow<IOState<User?>>(idle())

    val state: Flow<IOState<User?>> get() = _state.asStateFlow()

    fun register(username: String, email: String, password: String) {
        if (_state.value !is Idle)
            throw IllegalStateException("Cannot register while loading")
        _state.value = loading()
        viewModelScope.launch {
//            val response = userService.register(username, email, password)
//            if(response.isFailure)
//                _state.value = loadFailure(response.exceptionOrNull() ?: Exception("UNKNOWN"))
            val result = kotlin.runCatching {  User(username, email, password)}
            _state.value = loaded(result)



        }
    }



}