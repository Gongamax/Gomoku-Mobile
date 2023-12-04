package pt.isel.pdm.gomokuroyale.http.services

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import pt.isel.pdm.gomokuroyale.http.media.siren.SirenModel
import pt.isel.pdm.gomokuroyale.http.utils.makeAPIRequest

abstract class HTTPService(
    val httpClient: OkHttpClient,
    val jsonEncoder: Gson,
    val apiEndpoint: String
) {
    suspend inline fun <reified T> Request.getResponse(): SirenModel<T> =
        makeAPIRequest(httpClient, SirenModel.getType<T>().type, jsonEncoder)

    suspend inline fun <reified T> get(path: String): SirenModel<T> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .build()
            .getResponse()

    suspend inline fun <reified T> get(path: String, token: String): SirenModel<T> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .build()
            .getResponse()

    suspend inline fun <reified T> post(path: String, body: Any): SirenModel<T> {
        val request = Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .post(jsonEncoder.toJson(body).toRequestBody(contentType = applicationJsonMediaType))
            .build()
        val response: SirenModel<T> = request.getResponse<T>()
        return response
    }

    suspend inline fun <reified T> post(path: String, token: String, body: Any? = null): SirenModel<T> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .post(
                body = body?.let {
                    jsonEncoder.toJson(it).toRequestBody(contentType = applicationJsonMediaType)
                } ?: EMPTY_REQUEST
            )
            .build()
            .getResponse()

    suspend inline fun <reified T> put(path: String, token: String, body: Any = EMPTY_REQUEST): SirenModel<T> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .put(
                jsonEncoder.toJson(body)
                    .toRequestBody(contentType = applicationJsonMediaType)
            )
            .build()
            .getResponse()

    suspend inline fun <reified T> delete(path: String, token: String, body: Any = EMPTY_REQUEST): SirenModel<T> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .delete(
                jsonEncoder.toJson(body)
                    .toRequestBody(contentType = applicationJsonMediaType)
            )
            .build()
            .getResponse()

    companion object {
        const val APPLICATION_JSON = "application/json"
        val applicationJsonMediaType = APPLICATION_JSON.toMediaType()

        const val AUTHORIZATION_HEADER = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }
}
