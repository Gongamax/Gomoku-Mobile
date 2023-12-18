package pt.isel.pdm.gomokuroyale.http.domain.games

data class MatchmakeStatus(
    val mid: Int,
    val uid: Int,
    val gid: Int?,
    val state: String,
    val variant: String,
    val created: String,
    val pollingTimOut : Int
)
