package pt.isel.pdm.gomokuroyale.rankings.domain

import pt.isel.pdm.gomokuroyale.http.domain.users.UserRanking

/**
 * Represents the state of the ranking screen.
 * It can be in one of the following states:
 * - [FetchingRankingInfo]
 * - [FetchedRankingInfo]
 * - [FailedToFetchRankingInfo]
 * - [FetchingPlayerInfo]
 * - [FetchedPlayerInfo]
 * - [FailedToFetchPlayerInfo]
 * - [FetchingPlayersBySearch]
 * - [FetchedPlayersBySearch]
 * - [FailedToFetchPlayersBySearch]
 * - [WantsToGoToMatchHistory]
 */
sealed interface RankingScreenState {
    data object Idle : RankingScreenState
    data object FetchingRankingInfo : RankingScreenState

    data class FetchedRankingInfo(val rankingInfo: RankingState, val page : Int) : RankingScreenState

    data class FailedToFetchRankingInfo(val error: Throwable) : RankingScreenState

    data object FetchingPlayerInfo : RankingScreenState

    data class FetchedPlayerInfo(val playerInfo: UserRanking, val page: Int) : RankingScreenState

    data class FailedToFetchPlayerInfo(val error: Throwable) : RankingScreenState

    data object FetchingPlayersBySearch : RankingScreenState

    data class FetchedPlayersBySearch(val players: RankingState, val page : Int) : RankingScreenState

    data class FailedToFetchPlayersBySearch(val error: Throwable) : RankingScreenState

    data class WantsToGoToMatchHistory(val id: Int, val username: String, val page : Int) : RankingScreenState
}




fun Int.unitsConverter(): String {
    val points = this
    return when {
        points < 1000 -> points.toString()
        points < 1000000 -> String.format("%.1fK", points / 1000)
        else -> String.format("%.1fM", points / 1000000)
    }
}
data class RankingState(
    val rank: List<UserRanking>
)