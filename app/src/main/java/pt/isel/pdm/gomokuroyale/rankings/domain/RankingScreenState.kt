package pt.isel.pdm.gomokuroyale.rankings.domain

import pt.isel.pdm.gomokuroyale.http.domain.users.RankingList
import pt.isel.pdm.gomokuroyale.http.domain.users.UserRanking
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FailedToFetchPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FailedToFetchPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FailedToFetchRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchedPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchedPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchedRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchingPlayerInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchingPlayersBySearch
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.FetchingRankingInfo
import pt.isel.pdm.gomokuroyale.rankings.domain.RankingScreenState.WantsToGoToMatchHistory

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
    data class FetchedRankingInfo(val rankingInfo: RankingList, val page : Int) : RankingScreenState
    data class FailedToFetchRankingInfo(val error: Throwable) : RankingScreenState
    data object FetchingPlayerInfo : RankingScreenState
    data class FetchedPlayerInfo(val playerInfo: UserRanking, val page: Int) : RankingScreenState
    data class FailedToFetchPlayerInfo(val error: Throwable) : RankingScreenState
    data object FetchingPlayersBySearch : RankingScreenState
    data class FetchedPlayersBySearch(val players: RankingList, val page : Int) : RankingScreenState
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