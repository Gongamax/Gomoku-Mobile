package pt.isel.pdm.gomokuroyale.http.utils

sealed class APIResponse<out T> {
    data class Success<out T>(val value: T) : APIResponse<T>()
    data class Error(val error: FetchFromAPIException) : APIResponse<Nothing>()
}