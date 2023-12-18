package pt.isel.pdm.gomokuroyale.util

import kotlin.Error

sealed class HttpResult<out T> {
    data class Success<T>(val value: T) : HttpResult<T>()
    data class Failure(val error: ApiError) : HttpResult<Nothing>()
}

class ApiError(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

inline fun <T, R> HttpResult<T>.onSuccess(action: (T) -> HttpResult<R>): HttpResult<R> {
    return when (this) {
        is HttpResult.Success -> action(value)
        is HttpResult.Failure -> this
    }
}

inline fun <T> HttpResult<T>.onError(action: (ApiError) -> HttpResult<T>): HttpResult<T> {
    return when (this) {
        is HttpResult.Success -> this
        is HttpResult.Failure -> action(error)
    }
}

inline fun <T> HttpResult<T>.onSuccessResult(action: (T) -> Unit): HttpResult<T> {
    if (this is HttpResult.Success) action(value)
    return this
}

inline fun <T> HttpResult<T>.onFailureResult(action: (ApiError) -> Unit): HttpResult<T> {
    if (this is HttpResult.Failure) action(error)
    return this
}

//inline fun <T, R> HttpResult<T>.onSuccessResult(default: R, action: (T) -> R): R {
//    return when (this) {
//        is HttpResult.Success -> action(value)
//        is HttpResult.Failure -> default
//    }
//}
//
//inline fun <T, R> HttpResult<T>.onFailureResult(default: R, action: (ApiError) -> R): R {
//    return when (this) {
//        is HttpResult.Success -> default
//        is HttpResult.Failure -> action(error)
//    }
//}


fun <T> HttpResult<T>.isSuccess(): Boolean {
    return this is HttpResult.Success
}

fun <T> HttpResult<T>.isFailure(): Boolean {
    return this is HttpResult.Failure
}


