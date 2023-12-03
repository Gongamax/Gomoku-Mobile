package pt.isel.pdm.gomokuroyale.http.media.siren

import okhttp3.MediaType.Companion.toMediaType

/**
 * Siren is a hypermedia specification for representing entities in JSON.
 *
 * @property clazz is an array of strings that serves as an identifier for the link.
 * @property properties represent the properties of the entity.
 * @property entities represent sub-entities of the entity.
 * @property actions represent the available actions on the entity.
 * @property links represent navigational links, distinct from entity relationships.
 * @property requireAuth is a boolean that indicates if the entity requires authentication.
 * */

data class SirenModel<T>(
    val clazz: List<String>,
    val properties: T,
    val links: List<LinkModel>,
    val entities: List<EntityModel<*>>,
    val actions: List<ActionModel>,
    val requireAuth: List<Boolean>
) {
    companion object {
        private const val MEDIA_TYPE = "application/vnd.siren+json"

        val sirenMediaType = MEDIA_TYPE.toMediaType()
    }
}

