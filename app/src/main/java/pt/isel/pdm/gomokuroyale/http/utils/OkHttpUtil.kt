package pt.isel.pdm.gomokuroyale.http.utils

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import pt.isel.pdm.gomokuroyale.http.dto.DTO
import pt.isel.pdm.gomokuroyale.http.utils.Problem.Companion.problemMediaType
import kotlin.coroutines.resumeWithException

suspend fun <T> Request.makeAPIRequest(client: OkHttpClient, responseType : Class<out DTO>, jsonEncoder: Gson) : T =
    suspendCancellableCoroutine { continuation ->
        val newCall = client.newCall(this)
        newCall.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                continuation.resumeWithException(
                    FetchFromAPIException(
                        message = "Failed to send request to API",
                        cause = e
                    )
                )
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                val body = response.body ?: throw FetchFromAPIException(
                    "Error fetching from API, body is null. Remote service returned ${response.code}",
                    null
                )
                val contentType = body.contentType()
                val resJson = JsonReader(body.charStream())

                when {
                    response.isSuccessful -> {
                        Success<T>(//TODO: REMOVE DOUBLE BANG
                            jsonEncoder.fromJson(resJson, responseType)
                        )
                    }

                    !response.isSuccessful && contentType == problemMediaType ->
                        Failure(
                            jsonEncoder.fromJson<Problem>(resJson, Problem::class.java)
                        )

                    else ->
                        continuation.resumeWithException(
                            FetchFromAPIException(
                                "Error fetching from API. Remote service returned ${response.code}",
                                null
                            )
                        )
                }
            }
        })

        continuation.invokeOnCancellation { newCall.cancel() }
    }
