package pt.isel.pdm.gomokuroyale.http.services.games.models

import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameOutputModel
import pt.isel.pdm.gomokuroyale.http.media.siren.SirenModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameGetAllByUserOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameGetByIdOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingStatusOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameRoundOutputModel

typealias GetGameOutput = SirenModel<GameGetByIdOutputModel>

typealias PlayGameOutput = SirenModel<GameRoundOutputModel>

typealias SurrenderGameOutput = SirenModel<String>

typealias GetUserGamesOutput = SirenModel<GameGetAllByUserOutputModel>

typealias MatchmakingOutput = SirenModel<GameOutputModel>

typealias CancelMatchmakingOutput = SirenModel<String>

typealias GetMatchmakingStatusOutput = SirenModel<GameMatchmakingStatusOutputModel>

