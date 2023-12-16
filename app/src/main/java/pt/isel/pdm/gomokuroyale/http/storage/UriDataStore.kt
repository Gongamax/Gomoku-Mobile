package pt.isel.pdm.gomokuroyale.http.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.domain.Recipe
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository

class UriDataStore(
    private val gomokuService: GomokuService,
    private val store: DataStore<Preferences>
) : UriRepository {

    override suspend fun getRecipeLinks(): List<Recipe> =
        gomokuService.getHome().links.map { link ->
            Recipe(getRelName(link.rel.first()), link.href)
        }.also { recipes ->
            store.edit { preferences ->
                recipes.forEach { recipe ->
                    val key = stringPreferencesKey(recipe.rel)
                    preferences[key] = recipe.href
                }
            }
        }

    override suspend fun getRecipeLink(rel: String): Recipe? =
        store.data.first().let { preferences ->
            val href = preferences[stringPreferencesKey(rel)] ?: return null
            return Recipe(rel, href)
        }

    private fun getRelName(relUrl: String) = relUrl.split(DELIMITER).last()

    companion object {
        private const val DELIMITER = "/"
    }
}