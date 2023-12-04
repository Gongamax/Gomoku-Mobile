package pt.isel.pdm.gomokuroyale.rankings.domain

import pt.isel.pdm.gomokuroyale.http.domain.RankingEntry

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

sealed class RankingScreenState

data object FetchingRankingInfo : RankingScreenState()

data class FetchedRankingInfo(val rankingInfo : RankingState) : RankingScreenState()

data class FailedToFetchRankingInfo(val error : Throwable) : RankingScreenState()

data object FetchingPlayerInfo : RankingScreenState()

data class FetchedPlayerInfo(val playerInfo : RankingEntry) : RankingScreenState()

data class FailedToFetchPlayerInfo(val error : Throwable) : RankingScreenState()

data object FetchingPlayersBySearch : RankingScreenState()

data class FetchedPlayersBySearch(val players : RankingState) : RankingScreenState()

data class FailedToFetchPlayersBySearch(val error : Throwable) : RankingScreenState()

data class WantsToGoToMatchHistory(val id: Int, val username: String) : RankingScreenState()
fun Int.unitsConverter(): String {
    val points = this
    return when {
        points < 1000 -> points.toString()
        points < 1000000 -> String.format("%.1fK", points / 1000)
        else -> String.format("%.1fM", points / 1000000)
    }
}
data class RankingState(
    val rank: List<RankingEntry>
)