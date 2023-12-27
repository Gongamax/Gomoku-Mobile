package pt.isel.pdm.gomokuroyale.http.domain.users

data class UserRanking(
    val id: Int,
    val username: String,
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val rank: Int,
    val points: Int
)