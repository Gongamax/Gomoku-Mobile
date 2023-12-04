package pt.isel.pdm.gomokuroyale.http.services.users.models

import pt.isel.pdm.gomokuroyale.http.services.users.dto.RankingInfoOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserGetByIdOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserHomeOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserStatsOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserTokenCreateOutputModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserTokenRemoveOutputModel
import pt.isel.pdm.gomokuroyale.http.media.siren.SirenModel
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateOutputModel

typealias RegisterOutput = SirenModel<UserCreateOutputModel>

typealias LoginOutput = SirenModel<UserTokenCreateOutputModel>

typealias GetUserOutput = SirenModel<UserGetByIdOutputModel>

typealias GetUserHomeOutput = SirenModel<UserHomeOutputModel>

typealias LogoutOutput = SirenModel<UserTokenRemoveOutputModel>

typealias UpdateUserOutput = SirenModel<Unit>

typealias GetRankingOutput = SirenModel<RankingInfoOutputModel>

typealias GetStatsOutput = SirenModel<UserStatsOutputModel>