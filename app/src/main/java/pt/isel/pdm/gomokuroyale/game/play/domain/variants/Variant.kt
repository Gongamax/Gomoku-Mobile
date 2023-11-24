package pt.isel.pdm.gomokuroyale.game.play.domain.variants

import pt.isel.pdm.gomokuroyale.game.play.domain.BoardDim

enum class Variant(
    val boardDim: BoardDim,
    val openingRule: OpeningRule,
    val playingRule: PlayingRule
) : Variants {

    STANDARD(
        boardDim = BoardDim.STANDARD,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.STANDARD
    ),
    SWAP(
        boardDim = BoardDim.STANDARD,
        openingRule = OpeningRule.SWAP,
        playingRule = PlayingRule.STANDARD
    ),
    RENJU(
        boardDim = BoardDim.STANDARD,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.THREE_AND_THREE
    ),
    CARO(
        boardDim = BoardDim.STANDARD,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.STANDARD
    ),
    PENTE(
        boardDim = BoardDim.MODIFIED,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.STANDARD
    ),
    OMOK(
        boardDim = BoardDim.MODIFIED,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.THREE_AND_THREE
    ),
    NINUKI_RENJU(
        boardDim = BoardDim.STANDARD,
        openingRule = OpeningRule.STANDARD,
        playingRule = PlayingRule.THREE_AND_THREE
    );
}
