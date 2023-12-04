package pt.isel.pdm.gomokuroyale.http.utils

import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import pt.isel.pdm.gomokuroyale.http.media.Problem.Companion.problemMediaType
import pt.isel.pdm.gomokuroyale.http.media.siren.SirenModel.Companion.sirenMediaType
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend inline fun <reified T> Request.makeAPIRequest(
    client: OkHttpClient,
    responseType: Type,
    gson: Gson
): T =
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
                val resJson = body.string()

                when {
                    response.isSuccessful && contentType == sirenMediaType -> {
                        println("Response type: $responseType")
                        val res: T = gson.fromJson(resJson, responseType)
                        println(res)
                        //val success: Result<T> = Result.success(res)
                        continuation.resume(res)
                    }

                    !response.isSuccessful && contentType == problemMediaType ->
                        continuation.resumeWith(
                            Result.failure(Exception())//(gson.fromJson(resJson, Problem::class.java))
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
