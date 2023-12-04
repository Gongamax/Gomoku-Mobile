package pt.isel.pdm.gomokuroyale.http.services.users.dto

import pt.isel.pdm.gomokuroyale.http.domain.RankingEntry

class UserCreateOutputModel(
    val uid: Int
)

data class UserCreateInputModel(
    val username: String,
    val email: String,
    val password: String
)

data class UserCreateTokenInputModel(
    val username: String,
    val password: String
)

class UserGetByIdOutputModel(
    val id: Int,
    val username: String,
    val email: String
)

class UserStatsOutputModel(
    val id: Int,
    val username: String,
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val rank: Int,
    val points: Int
)

class UserHomeOutputModel(
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