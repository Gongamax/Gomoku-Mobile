package pt.isel.pdm.gomokuroyale.http.dto.util

data class RankingEntry (
    val id : Int,
    val username: String,
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val points: Int,
    val rank: Int
)

fun Int.unitsConverter(): String {
    val points = this
    return when {
        points < 1000 -> points.toString()
        points < 1000000 -> String.format("%.1fK", points / 1000)
        else -> String.format("%.1fM", points / 1000000)
    }
}