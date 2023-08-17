package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

open class SquareBoardImpl(final override val width: Int) : SquareBoard {

    var cells: Array<Array<Cell>> = arrayOf()

    init {
        (1..width).forEach { i ->
            var row = arrayOf<Cell>()
            (1..this.width).forEach { j ->
                row += Cell(i, j)
            }
            cells += row
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return when {
            i > width || j > width || i == 0 || j == 0 -> null
            else -> getCell(i, j)
        }
    }

    override fun getCell(i: Int, j: Int): Cell {
        return cells[i - 1][j - 1]
    }

    override fun getAllCells(): Collection<Cell> {
       return cells.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return if (jRange.last > width) {
            IntRange(jRange.first, width).map {
                    j: Int -> getCell(i, j)
            }.toList()
        } else {
            jRange.map {
                    j: Int -> getCell(i, j)
            }.toList()
        }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return if (iRange.last > width) {
            IntRange(iRange.first, width).map {
                    i: Int -> getCell(i, j)
            }.toList()
        } else {
            iRange.map {
                    i: Int -> getCell(i, j)
            }.toList()
        }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        val (i, j) = when (direction) {
            Direction.UP -> Pair(i - 1, j)
            Direction.DOWN -> Pair(i + 1, j)
            Direction.LEFT -> Pair(i, j - 1)
            Direction.RIGHT -> Pair(i, j + 1)
        }

        return getCellOrNull(i, j)
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {

    private val cellsBoard = mutableMapOf<Cell, T?>()

    init {
        cells.forEach { unit -> unit.forEach { cell -> cellsBoard[cell] = null } }
    }

    override fun get(cell: Cell): T? {
        return cellsBoard[cell]
    }

    override fun set(cell: Cell, value: T?) {
        cellsBoard[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cellsBoard.filter { (_, value) -> predicate(value) }.map { (cell, _) -> cell }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return filter(predicate).firstOrNull()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return find(predicate) != null
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return filter(predicate).size == cellsBoard.size
    }
}


