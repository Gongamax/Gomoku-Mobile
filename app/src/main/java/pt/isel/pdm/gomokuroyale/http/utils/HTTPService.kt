package pt.isel.pdm.gomokuroyale.http.utils

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import pt.isel.pdm.gomokuroyale.http.dto.DTO

typealias GetResponseResult = Either<FetchFromAPIException, DTO>

abstract class HTTPService(
    val httpClient: OkHttpClient,
    val jsonEncoder: Gson,
    val apiEndpoint: String
) {
    suspend inline fun Request.getResponse(responseType: Class<out DTO>): APIResponse<DTO> =
        makeAPIRequest(httpClient, responseType, jsonEncoder)

    suspend inline fun get(path: String, responseType: Class<out DTO>): APIResponse<DTO> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .build()
            .getResponse(responseType)

    suspend inline fun get(
        path: String,
        responseType: Class<out DTO>,
        token: String
    ): APIResponse<DTO> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .addHeader(AUTHORIZATION_HEADER, "$TOKEN_TYPE $token")
            .build()
            .getResponse(responseType)

    suspend inline fun post(
        path: String,
        responseType: Class<out DTO>,
        body: Any
    ): APIResponse<DTO> =
        Request.Builder()
            .url("$apiEndpoint/$path")
            .addHeader("accept", APPLICATION_JSON)
            .post(jsonEncoder.toJson(body).toRequestBody(contentType = applicationJsonMediaType))
            .build()
            .getResponse(responseType)

    suspend inline fun post(
        path: String,
        responseType: Class<out DTO>,
        token: String,
        body: Any? = null
    ): APIResponse<DTO> =
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

    suspend inline fun put(
        path: String,
        responseType: Class<out DTO>,
        token: String,
        body: Any
    ): APIResponse<DTO> =
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

    suspend inline fun delete(
        path: String,
        responseType: Class<out DTO>,
        token: String,
        body: Any
    ): APIResponse<DTO> =
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