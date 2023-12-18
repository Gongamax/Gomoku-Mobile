package pt.isel.pdm.gomokuroyale.http.domain.games

import pt.isel.pdm.gomokuroyale.authentication.domain.User
import pt.isel.pdm.gomokuroyale.game.play.domain.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant

data class Game(
    val id : Int,
    val players : Pair<User, User>,
    val board : Board,
    val state : String,
    val variant: Variant
)
