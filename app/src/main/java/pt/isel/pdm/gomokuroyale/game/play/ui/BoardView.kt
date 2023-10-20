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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.game.play.domain.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.BoardRun
import pt.isel.pdm.gomokuroyale.game.play.domain.Cell
import pt.isel.pdm.gomokuroyale.game.play.domain.Player

const val BOARD_DIM = 17

val cellSize = 20.dp

//val lineSize = 0.5.dp //Atualmente não está a ser usado LineSize
val boardSize = cellSize * BOARD_DIM //+ (cellSize/2) * (BOARD_DIM)

const val BROWN_COLOR_CODE = 0xFF8B4513

const val PIECE_TEST_TAG = "PIECE"

/**
 * Board still needs some adjustments in design, but almost there!
 */
@Composable
fun BoardView(
    board: Board?,
    onClick: (Cell) -> Unit
) {
    if (board == null) Log.v("View", "The Board is null")
    else
        Box(Modifier.border(width = 5.dp, color = Color.Cyan)) {
            Column(
                modifier = Modifier
                    .width(boardSize)
                    .testTag("Board"),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                repeat(BOARD_DIM) { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        repeat(BOARD_DIM) { col ->
                            if (row == 0) {
                                when (col) {
                                    0 -> Border(resourceId = R.drawable.top_left_corner_border)
                                    BOARD_DIM - 1 -> Border(resourceId = R.drawable.top_right_corner_border)
                                    else -> Border(resourceId = R.drawable.top_border)
                                }
                            } else if (row == BOARD_DIM - 1) {
                                when (col) {
                                    0 -> Border(resourceId = R.drawable.bottom_left_corner_border)
                                    BOARD_DIM - 1 -> Border(resourceId = R.drawable.bottom_right_corner_border)
                                    else -> Border(resourceId = R.drawable.bottom_border)
                                }
                            } else if (col == BOARD_DIM - 1)
                                Border(resourceId = R.drawable.right_border)
                            else if (col == 0) {
                                Border(resourceId = R.drawable.left_border)
                            } else {
                                val pos = Cell(row - 1, col - 1)
                                CellView(
                                    player = board.moves[pos],
                                    onClick = {
                                        onClick(pos); Log.v(
                                        "Piece",
                                        "(${pos.row.index}, ${pos.col.index})"
                                    )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
}

@Composable
fun CellView(player: Player?, onClick: () -> Unit) {
    val mod = Modifier
        .size(cellSize)
        .background(Color(BROWN_COLOR_CODE))
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
                Player.WHITE -> R.drawable.white_circle
                Player.BLACK -> R.drawable.black_circle
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
fun Border(resourceId: Int) {
    Box(
        modifier = Modifier
            .size(cellSize)
            .background(Color(BROWN_COLOR_CODE))
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "top"
        )
    }
}

@Composable
@Preview
fun BoardViewPreview() {
    BoardView(BoardRun(emptyMap(), Player.BLACK), onClick = {})
}