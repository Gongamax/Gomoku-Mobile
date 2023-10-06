package pt.isel.pdm.gomokuroyale

import org.junit.*
import org.junit.Test
import pt.isel.pdm.gomokuroyale.game.play.domain.Cell
import pt.isel.pdm.gomokuroyale.game.play.domain.Column
import pt.isel.pdm.gomokuroyale.game.play.domain.Row

class CellTests {

    @Test
    fun testCell() {
        val cell = Cell(0, 0)
        Assert.assertEquals(Row(1), cell.row)
        Assert.assertEquals(Column('A'), cell.col)
    }

    @Test
    fun testCellEquals() {
        val cell1 = Cell(1, 1)
        val cell2 = Cell(1, 1)
        Assert.assertEquals(cell1, cell2)
    }

    @Test
    fun testCellInvalid() {
        val cell = Cell(-1, -1)
        Assert.assertEquals(cell, Cell.INVALID)
    }
}