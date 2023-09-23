package pt.isel.pdm.gomokuroyale.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.model.Board
import pt.isel.pdm.gomokuroyale.model.Cell
import pt.isel.pdm.gomokuroyale.model.Player

const val BOARD_DIM = 15

val cellSize = 20.dp

//val lineSize = 0.5.dp //Atualmente não está a ser usado LineSize
val boardSize = cellSize * BOARD_DIM //+ (cellSize/2) * (BOARD_DIM)

const val BROWN_COLOR_CODE = 0xFF8B4513

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
        Column(
            modifier = Modifier
                .width(boardSize)
                .padding(cellSize / 2),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            repeat(BOARD_DIM) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    repeat(BOARD_DIM) { col ->
                        val pos = Cell(row, col)
                        CellView(board.moves[pos]) { onClick(pos) }
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
    if (player == null)
        Box(
            modifier = mod
                .clip(CircleShape)
                .clickable(onClick = onClick)
        ) {
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