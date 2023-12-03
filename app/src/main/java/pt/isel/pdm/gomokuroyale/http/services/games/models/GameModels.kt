package pt.isel.pdm.gomokuroyale.http.services.games.models

import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameOutputModel
import pt.isel.pdm.gomokuroyale.http.media.siren.SirenModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameGetAllByUserOutputModel
import pt.isel.pdm.gomokuroyale.http.services.games.dto.GameMatchmakingStatusOutputModel

typealias GetGameOutput = SirenModel<GameOutputModel>

typealias PlayGameOutput = SirenModel<GameOutputModel>

typealias SurrenderGameOutput = SirenModel<Unit>

typealias GetUserGamesOutput = SirenModel<GameGetAllByUserOutputModel>

typealias MatchmakingOutput = SirenModel<GameOutputModel>

typealias CancelMatchmakingOutput = SirenModel<Unit>

typealias GetMatchmakingStatusOutput = SirenModel<GameMatchmakingStatusOutputModel>

