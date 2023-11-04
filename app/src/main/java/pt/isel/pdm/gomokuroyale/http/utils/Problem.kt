package pt.isel.pdm.gomokuroyale.http.utils

import okhttp3.MediaType.Companion.toMediaType
import java.net.URI

class Problem(
    val type: URI,
    val title: String,
    val status: Int,
    val detail: String? = null,
    val instance: URI? = null
) {
    companion object {
        private const val MEDIA_TYPE = "application/problem+json"

        val problemMediaType = MEDIA_TYPE.toMediaType()
    }
}