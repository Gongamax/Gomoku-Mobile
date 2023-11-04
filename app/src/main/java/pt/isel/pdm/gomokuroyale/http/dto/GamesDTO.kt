package pt.isel.pdm.gomokuroyale.http.dto

import pt.isel.pdm.gomokuroyale.authentication.domain.User
import pt.isel.pdm.gomokuroyale.game.play.domain.Board
import pt.isel.pdm.gomokuroyale.http.dto.util.MatchmakingStatus

data class GamePlayInputModel(val row: Int, val column: Int) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

data class GameRoundOutputModel(val game: GameOutputModel, val state: String) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

data class GameOutputModel(
    val id: Int,
    val board: Board,
    val userBlack: User,
    val userWhite: User
) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

data class GameGetByIdOutputModel(val game: GameOutputModel) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

data class GameMatchmakingInputModel(val variant: String) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

data class GameMatchmakingStatusOutputModel(val status: MatchmakingStatus) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

data class GameGetAllOutputModel(val games: List<GameOutputModel>) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

data class GameGetAllByUserOutputModel(val games: List<GameOutputModel>) : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}

// TODO: Remove this class, TEMPORARY
class GameEmptyOutputModel() : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}