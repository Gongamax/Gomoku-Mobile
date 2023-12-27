package pt.isel.pdm.gomokuroyale.http.services.users.dto

import pt.isel.pdm.gomokuroyale.http.dto.DTO
import pt.isel.pdm.gomokuroyale.http.dto.util.RankingEntry

data class UserCreateInputModel(
    val username: String,
    val email: String,
    val password: String
) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

data class UserCreateTokenInputModel(
    val username: String,
    val password: String
) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

class UserGetByIdOutputModel(
    val id: Int,
    val username: String,
    val email: String
) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

class UserStatsOutputModel(
    val id: Int,
    val username: String,
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val rank: Int,
    val points: Int
) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

class UserHomeOutputModel(
    val id: Int,
    val username: String
) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

data class UserTokenCreateOutputModel(
    val token: String
) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

data class RankingInfoOutputModel(
    val rankingTable: List<RankingEntry>
) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}


data class UserTokenRemoveOutputModel(
    val message: String
) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

//TODO: remove this class, it's only here to make the compiler happy TEMPORARILY
data class UserEmptyOutputModel(
    val classList: List<String>,
    val properties: Properties,
    val links: List<Link>,
    val entities: List<Any>,
    val actions: List<Any>,
    val requireAuth: List<Boolean>
) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

data class Properties(
    val uid: Int
)

data class Link(
    val rel: List<String>,
    val href: String
)
