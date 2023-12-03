package pt.isel.pdm.gomokuroyale.http.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.EMPTY_REQUEST
import pt.isel.pdm.gomokuroyale.http.dto.DTO
import pt.isel.pdm.gomokuroyale.http.media.siren.SirenModel

abstract class HTTPService(
    val httpClient: OkHttpClient,
    val jsonEncoder: Gson,
    val apiEndpoint: String
) {
    suspend inline fun <T> Request.getResponse(responseType: Class<out DTO>): Result<SirenModel<T>> =
        makeAPIRequest(httpClient, responseType, jsonEncoder) as Result<SirenModel<T>>

    suspend inline fun <T> get(path: String, responseType: Class<out DTO>): Result<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .build()
            .getResponse(responseType)

    suspend inline fun <T> get(
        path: String,
        responseType: Class<out DTO>,
        token: String
    ): Result<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .build()
            .getResponse(responseType)

    suspend inline fun <T> post(
        path: String,
        responseType: Class<out DTO>,
        body: Any
    ): Result<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .post(jsonEncoder.toJson(body).toRequestBody(contentType = applicationJsonMediaType))
            .build()
            .getResponse(responseType)

    suspend inline fun <T> post(
        path: String,
        responseType: Class<out DTO>,
        token: String,
        body: Any? = null
    ): Result<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .post(
                body = body?.let {
                    jsonEncoder.toJson(it).toRequestBody(contentType = applicationJsonMediaType)
                } ?: EMPTY_REQUEST)
            .build()
            .getResponse(responseType)

    suspend inline fun <T> put(
        path: String,
        responseType: Class<out DTO>,
        token: String,
        body: Any = EMPTY_REQUEST
    ): Result<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .put(
                jsonEncoder.toJson(body)
                    .toRequestBody(contentType = applicationJsonMediaType)
            )
            .build()
            .getResponse(responseType)

    suspend inline fun <T> delete(
        path: String,
        responseType: Class<out DTO>,
        token: String,
        body: Any = EMPTY_REQUEST
    ): Result<SirenModel<T>> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .delete(
                jsonEncoder.toJson(body)
                    .toRequestBody(contentType = applicationJsonMediaType)
            )
            .build()
            .getResponse(responseType)

    companion object {
        const val APPLICATION_JSON = "application/json"
        val applicationJsonMediaType = APPLICATION_JSON.toMediaType()

        const val AUTHORIZATION_HEADER = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }
}