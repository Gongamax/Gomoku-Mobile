package pt.isel.pdm.gomokuroyale

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.game.play.domain.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.BoardRun
import pt.isel.pdm.gomokuroyale.game.play.domain.Player
import pt.isel.pdm.gomokuroyale.game.play.ui.BoardView

class BoardInstrumentalTests {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testBoardView() {
        //Arrange
        val board: Board = BoardRun(emptyMap(), Player.BLACK)
        //Act
        composeRule.setContent {
            BoardView(board, onClick = {})
        }
        //Assert
        composeRule.onNodeWithTag("Board").assertExists()
    }
}