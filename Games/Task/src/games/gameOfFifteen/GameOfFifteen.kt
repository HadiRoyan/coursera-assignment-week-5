package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(val initializer: GameOfFifteenInitializer) : Game {

    val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val cells = board.getAllCells()
        val permutation = initializer.initialPermutation
    
        cells.forEachIndexed { index, cell ->
            board[cell] = permutation.getOrNull(index)
        }
    }

    override fun canMove(): Boolean {
       return true;
    }

    override fun hasWon(): Boolean {
        var seq = 1
        return board.all { it == null || it == seq++ }
    }

    override fun processMove(direction: Direction) {
        val emptyCell = board.find { it == null }!!

        val neighbour : Cell?
        board.apply { neighbour = emptyCell.getNeighbour(direction.reversed()) }

        if (neighbour == null) {
            return
        }

        board[emptyCell] = board[neighbour!!]
        board[neighbour!!] = null

    }

    override fun get(i: Int, j: Int): Int? {
        return board[board.getCell(i, j)]
    }

}
