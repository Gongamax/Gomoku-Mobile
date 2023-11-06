package pt.isel.pdm.gomokuroyale.game.play.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.game.play.domain.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.BoardDim
import pt.isel.pdm.gomokuroyale.game.play.domain.BoardRun
import pt.isel.pdm.gomokuroyale.game.play.domain.Cell
import pt.isel.pdm.gomokuroyale.game.play.domain.Player
import pt.isel.pdm.gomokuroyale.ui.theme.AlabasterWhite
import pt.isel.pdm.gomokuroyale.ui.theme.Brown

/*const val BOARD_DIM = 15*/

/*val cellSize = 24.5.dp*/

/*
val displayMetrics = LocalContext.current.resources.displayMetrics
val screenWidth = displayMetrics.widthPixels
val screenHeight = displayMetrics.heightPixels
*/

//val lineSize = 0.5.dp //Atualmente não está a ser usado LineSize
//val boardSize = cellSize * BOARD_DIM //+ (cellSize/2) * (BOARD_DIM)

val boarderSize = 5.dp

const val BROWN_COLOR_CODE = 0xFFC39F60
const val WHITE_COLOR = 0xFFEDEADE

const val PIECE_TEST_TAG = "PIECE"

/**
 * Board still needs some adjustments in design, but almost there!
 */
@Composable
fun BoardView(
    board: Board?,
    boardDim: Int,
    onClick: (Cell) -> Unit,
) {

    if (board == null) Log.v("View", "The Board is null")
    else {
        /*  var currentWidth by remember(board) { mutableStateOf(0) }*/
        val maxWidth = LocalConfiguration.current.screenWidthDp.dp
        Box(
            Modifier
                .border(width = boarderSize, color = AlabasterWhite)
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    /*.onGloballyPositioned { coordinates ->
                        currentWidth = coordinates.size.width
                    }*/
                    .testTag("Board"),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                BorderConstructor(
                    board,
                    boardDim,
                    onClick,
                    maxWidth = maxWidth
                )
                /* maxWidth = LocalDensity.current.run {  currentWidth.toDp() })*/
            }
        }
    }
}

@Composable
private fun BorderConstructor(
    board: Board,
    boardDim: Int,
    onClick: (Cell) -> Unit,
    maxWidth: Dp
) {
    val cellSize = maxWidth / (boardDim)
    repeat(boardDim) { row ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            repeat(boardDim) { col ->
                val resourceId = getResourceId(Cell(row, col), boardDim)
                if (resourceId != 0)
                    Border(resourceId, cellSize)
                else {
                    val pos = Cell(row - 1, col - 1)
                    CellView(board.moves[pos], cellSize) {
                        onClick(pos)
                        Log.v("Piece", "(${pos.row.index}, ${pos.col.index})")
                    }
                }
            }
        }
    }

}


@Composable
fun CellView(player: Player?, cellSize: Dp, onClick: () -> Unit) {
    val mod = Modifier
        .size(cellSize)
        .background(Brown)
        .testTag(PIECE_TEST_TAG)
    if (player == null)
        Box(
            modifier = mod
                .clip(CircleShape)
                .clickable { onClick() }
        ) {
            Log.v("Piece", "Inside if on CellView")
            val resourceId = R.drawable.cross
            Image(painter = painterResource(id = resourceId), contentDescription = "Cross")
        }
    else
        Box(
            modifier = mod,
            contentAlignment = Alignment.Center
        ) {
            var fill by remember(player) { mutableStateOf(0.1f) }
            val image = when (player) {
                Player.WHITE -> R.drawable.checkers_white
                Player.BLACK -> R.drawable.checkers_black
            }
            Image(
                painter = painterResource(image),
                modifier = Modifier.fillMaxSize(fill),
                contentDescription = "image"
            )
            LaunchedEffect(player) {
                while (fill < 1f) {
                    delay(50)
                    fill *= 2
                }
                fill = 1f
            }
        }

}

@Composable
fun Border(resourceId: Int, cellSize: Dp) {
    Box(
        modifier = Modifier
            .size(cellSize)
            .background(Brown)
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "top"
        )
    }
}

private fun getResourceId(cell: Cell, boardDim: Int): Int {
    val isTopRow = cell.row.index == 0
    val isBottomRow = cell.row.index == boardDim - 1
    val isLeftCol = cell.col.index == 0
    val isRightCol = cell.col.index == boardDim - 1

    return when {
        isTopRow && isLeftCol -> R.drawable.top_left_corner_border
        isTopRow && isRightCol -> R.drawable.top_right_corner_border
        isBottomRow && isLeftCol -> R.drawable.bottom_left_corner_border
        isBottomRow && isRightCol -> R.drawable.bottom_right_corner_border
        isTopRow -> R.drawable.top_border
        isBottomRow -> R.drawable.bottom_border
        isLeftCol -> R.drawable.left_border
        isRightCol -> R.drawable.right_border
        else -> 0
    }
}

@Composable
@Preview
fun BoardViewPreview() {
    BoardView(BoardRun(emptyMap(), Player.BLACK), BoardDim.STANDARD.toInt(), onClick = {})
}












