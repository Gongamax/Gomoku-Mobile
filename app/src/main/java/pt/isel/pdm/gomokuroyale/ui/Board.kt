package pt.isel.pdm.gomokuroyale.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R

const val BOARD_DIM = 15

val cellSize = 24.dp
//val lineSize = 0.5.dp //Atualmente não está a ser usado LineSize
val boardSize = cellSize * BOARD_DIM /*+ lineSize * (BOARD_DIM)*/

const val BROWN_COLOR_CODE = 0xFF8B4513

/**
 * Board still needs some adjustments in design, but almost there!
 */
@Composable
fun BoardView() {
    Column(
        modifier = Modifier
            .width(boardSize),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        repeat(BOARD_DIM) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                repeat(BOARD_DIM) { col ->
                    CellView()
                }
            }
        }
    }
}

@Composable
fun CellView() {
    val mod = Modifier
        .size(cellSize)
        .background(Color(BROWN_COLOR_CODE))
    Box(modifier = mod.clickable(onClick = { println("Clicked") })) {
        val resourceId = R.drawable.cross
        Image(painter = painterResource(id = resourceId), contentDescription = "Cross")
    }
}