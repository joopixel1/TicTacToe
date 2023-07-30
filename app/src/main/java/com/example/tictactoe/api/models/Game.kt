package com.example.tictactoe.api.models

import com.example.tictactoe.api.enums.Mode
import com.example.tictactoe.api.enums.PlayerType
import com.example.tictactoe.api.enums.State
import java.util.Random

/**
 * A game object for a new game
 *
 * @param mode SingleMode OR MultiMode
 * @param finished if game has ended default false
 * @param size the size of the board default 3
 */
internal class Game(
    private val mode: Mode,
    private val size: Int = 3,
) {

    private val rand = Random()
    private var computer: PlayerType? = if (rand.nextInt(2) == 0) PlayerType.X else PlayerType.O
    private var gameState = GameState(computer!!, size, 0, State.NOT_DONE)
    /**
     * I was thinking dat instead of calculating the recursion everytime for the computer play.
     * We could do it once and store it in a tree. Then use dis values for the rest of the game.
     */
    private var computerTree: CustomTree<Pair<Int, Int?>>? = null

    var finished: Boolean = false
        get() = gameState.state != State.NOT_DONE
    var currentGrid: String = ""
        get() = gameState.gameGrid
    var winner: State? = null
        get() = if (finished) (if (gameState.state == computer?.state) State.COMPUTER_WINS_U_DUNS else gameState.state) else null


    init {
        if (mode == Mode.SINGLEPLAYER) {
            val compFirst = rand.nextInt(2) == 0
            computerTree = gameState.calculateTree(gameState.gameGrid.toCharArray(), compFirst, 0)
            if (compFirst) gameState = gameState.playComputer(computerTree!!)
            else computer = computer?.next?.value
        }
        else computer = null
    }


    override fun toString(): String = gameState.toString()

    /**
     * makes the next player at location index i
     *
     * @param i location
     * @throws Exception if index already filled
     */
    fun play(i: Int) {
        if (mode == Mode.SINGLEPLAYER) {
            gameState = gameState.playHuman(i, computerTree)
                .playComputer(computerTree!!)
        }
        else {
            gameState = gameState.playHuman(i, computerTree)
        }
    }

}