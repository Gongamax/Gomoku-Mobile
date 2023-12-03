package pt.isel.pdm.gomokuroyale.authentication.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.authentication.domain.validateUsername
import pt.isel.pdm.gomokuroyale.http.UserService
import pt.isel.pdm.gomokuroyale.util.IOState
import pt.isel.pdm.gomokuroyale.util.Idle
import pt.isel.pdm.gomokuroyale.util.Loading
import pt.isel.pdm.gomokuroyale.util.Saving
import pt.isel.pdm.gomokuroyale.util.idle
import pt.isel.pdm.gomokuroyale.util.loading
import pt.isel.pdm.gomokuroyale.util.saved
import pt.isel.pdm.gomokuroyale.util.saving


class LoginScreenViewModel(
    private val userInfoRepository: UserInfoRepository,
    private val userService: UserService,
) : ViewModel() {

    companion object {
        fun factory(
            userInfoRepository: UserInfoRepository,

            userService: UserService
        ) = viewModelFactory {
            initializer { LoginScreenViewModel(userInfoRepository, userService) }
        }
    }

    private val _state = MutableStateFlow<IOState<UserInfo?>>(idle())

    val state: Flow<IOState<UserInfo?>> get() = _state.asStateFlow()


    // TODO - Check if return a problem

    fun login(username: String, password: String) {
        if (_state.value !is Idle)
            throw IllegalStateException("Cannot login while loading")
        _state.value = loading()
        viewModelScope.launch {

//            val response = userService.login(username, password)
//            if (response.isFailure)
//                _state.value = loadFailure(response.exceptionOrNull() ?: Exception("UNKNOWN"))
//
//            val body = response.getOrNull() as UserTokenCreateOutputModel
//            val userInfo = UserInfo(body.token, username)
            val userInfo = UserInfo("token", username)
            _state.value = saving()
            val result = kotlin.runCatching { userInfoRepository.login(userInfo); userInfo }
            _state.value = saved(result)

        }
    }

    fun resetToIdle() {
        if (_state.value !is Saving && _state.value !is Loading)
            throw IllegalStateException("Cannot reset to idle while loading")
        _state.value = idle()
    }

    fun logout() {
        viewModelScope.launch {
            userInfoRepository.logout()
        }
    }

}