package pt.isel.pdm.gomokuroyale.http.services.users.dto

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import pt.isel.pdm.gomokuroyale.http.domain.users.RankingEntry
import pt.isel.pdm.gomokuroyale.util.UserStatsDeserializer
import java.lang.reflect.Type

// region Input Models

data class UserCreateInputModel(
    val username: String,
    val email: String,
    val password: String
)

data class UserCreateTokenInputModel(
    val username: String,
    val password: String
)

// endregion

// region Output Models

data class UserCreateOutputModel(
    val uid: Int
)

data class UserGetByIdOutputModel(
    val id: Int,
    val username: String,
    val email: String
)

data class UserStatsOutputModel(
    val uid: Int,
    val username: String,
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val rank: Int,
    val points: Int
) {


    companion object {
        fun getCustomGson(): Gson {
            return GsonBuilder()
                .registerTypeAdapter(UserStatsOutputModel::class.java, UserStatsDeserializer())
                .create()
        }
    }
}

data class UserHomeOutputModel(
    val id: Int,
    val username: String
)

data class UserTokenCreateOutputModel(
    val token: String
)

data class RankingInfoOutputModel(
    val rankingTable: List<RankingEntry>
)

data class UserTokenRemoveOutputModel(
    val message: String
)

// endregion