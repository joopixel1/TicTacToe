package com.example.tictactoe.api.models

import com.example.tictactoe.api.enums.PlayerType
import com.example.tictactoe.api.enums.State
import java.lang.Exception

internal class GameState(
    private val playerTurn: PlayerType,
    private val size: Int,
    private val boxFill: Int,
    val state: State,
    val gameGrid: String = "         ",
){

    init {
        if (gameGrid.length != size*size) throw IllegalArgumentException()
    }

    override fun toString(): String {
        fun forms(word: String): String = word.substring(0, 3) + "\n" + word.substring(3, 6) + "\n" + word.substring(6, 9) + "\n"
        return "${forms(gameGrid)}player${playerTurn}"
    }

    /**
     * Extension function to return a list of indexes of occurrence of a character in a string
     *
     * @param Char character u wanna check for
     * @return List<Int> list of all the indexes
     */
    private fun CharArray.indicesOf(c: Char): List<Int> {
        val ans = ArrayList<Int>()
        this.forEachIndexed { index, character -> if (c == character) ans.add(index) }
        return ans
    }

    /**
     * For human player: Inputs the players character play at specific index, if available
     *
     * @param i index in grid
     * @returns a new GameState with next player and updates gameGrid
     * @throws Exception("Spot already filled")
     */
    fun playHuman(i: Int, computerTree: CustomTree<Pair<Int, Int?>>?): GameState {
        if (state == State.NOT_DONE) {

            if (gameGrid[i] != ' ') throw Exception("Spot already filled")
            if(computerTree != null){
                var ind: Int? = null
                computerTree.children(computerTree.root).forEachIndexed{loc, child ->
                    if(i == child.element!!.first) ind = loc
                }
                computerTree.setRootChildToRoot(ind!!)
            }
            val newGrid: String = gameGrid.substring(0, i) + playerTurn.name + gameGrid.substring(i + 1)
            return GameState(playerTurn.next.value, size, boxFill+1, done(newGrid, i, playerTurn, boxFill+1), newGrid)

        }
        else return this
    }

    /**
     * For Computer player: Inputs the players character play at specific index, if available.
     * It determines the right index to put the character through different algorithms.
     *
     * @returns a new GameState with next player and updates gameGrid
     * @throws Exception("Spot already filled")
     */
    fun playComputer(computerTree: CustomTree<Pair<Int, Int?>>): GameState {
        if (state == State.NOT_DONE) {

            //OLD WAY THAT INVOLVED RECURSION AND FINDING THE BEST SOLUTION EACH TIME
//            val i = computeMinimax(gameGrid.toCharArray())
//            if (gameGrid[i] != ' ') throw Exception("COMPUTERS CALCULATION COMPROMISED")


            val i = getOptimalPlayFromTree(computerTree)
            if (gameGrid[i.first] != ' ') throw Exception("COMPUTERS CALCULATION COMPROMISED")
            computerTree.setRootChildToRoot(i.second)
            val newGrid: String = gameGrid.substring(0, i.first) + playerTurn.name + gameGrid.substring(i.first + 1)
            return GameState(playerTurn.next.value, size, boxFill+1, done(newGrid, i.first, playerTurn, boxFill+1), newGrid)


        }
        else return this
    }

    /**
     * It creates a custom tree using recursion technique for minimax
     * inorder to create all possible solutions in a tree.
     */
    fun calculateTree(grid: CharArray, compFirst: Boolean, fill: Int): CustomTree<Pair<Int, Int?>> {
        val computerTree: CustomTree<Pair<Int, Int?>> = CustomTree()
        val locations = grid.indicesOf(' ')
        val root= computerTree.root
        for( i in locations) {
            grid[i] = playerTurn.name[0]
            val pos = computerTree.addChild(root, Pair(i, null))
            val temp = minimaxToTree(grid, playerTurn.next.value, compFirst, done(grid.concatToString(), i, playerTurn, fill+1), computerTree, pos, fill+1)
            computerTree.setPos(pos, Pair(pos.element!!.first,temp))
            grid[i] = ' '
        }
        return computerTree
    }



    private fun getOptimalPlayFromTree(computerTree: CustomTree<Pair<Int, Int?>>): Pair<Int, Int> {
        val children: List<CustomTree.Pos<Pair<Int, Int?>>> = computerTree.children(computerTree.root)
        var lowestChildren: ArrayList<Pair<Int, Int>> = ArrayList()
        var max = Int.MIN_VALUE
        for( i in children.indices){
            if(max < children[i].element!!.second!!) {
                lowestChildren = ArrayList()
                lowestChildren.add(Pair(children[i].element!!.first, i))
                max = children[i].element!!.second!!
            }
            else if(max == children[i].element!!.second!!) lowestChildren.add(Pair(children[i].element!!.first, i))
        }
        return lowestChildren.random()
    }

    /**
     * checks whether the current gameState is finished with anybody winning
     *
     * @return State.?_Won, if currentplayer won.
     *          if no winner:
     *          it returns State.DRAW if gameState is finished and State.NOT_DONE if gameState not finished
     */
    private fun done(grid: String, loc: Int, player: PlayerType, fill: Int): State {

        if( fill <= (size-1)*2) return State.NOT_DONE

        fun checkRow(num: Int): Boolean {
            val start = (num/size)*size
            for(i in 0 until size)
                 if(grid[start+i] != player.name[0]) return false

            return true
        }
        fun checkColumn(num: Int): Boolean {
            val start = num%size
            for(i in start until size*size step size)
                if(grid[i] != player.name[0]) return false

            return true
        }
        fun checkDialog(num: Int): Boolean {

            fun even(a: Int): Boolean = a%2==0
            fun checkWindowsDialog(a: Int): Boolean = a%(size+1)==0
            fun checkUnixDialog(a: Int): Boolean = a!=0 && a!=(size*size-1) && a%(size-1)==0

            if (even(size)) return false
            if (checkWindowsDialog(num)) {
                val start = 0
                for(i in start until size*size step size+1)
                    if(grid[i] != player.name[0]) return false

                return true
            }
            if(checkUnixDialog(num)) {
                val start = size-1
                for(i in start until size*size-1 step size-1)
                    if(grid[i] != player.name[0]) return false

                return true
            }
            return false
        }

        val bool = checkRow(loc)
                || checkColumn(loc)
                || checkDialog(loc)
        return if ( bool ) player.state
            else if(fill < 9) State.NOT_DONE
            else State.DRAW
    }

    /**
     * calculate the optimal next location to move using minimax theorem
     */
    private fun computeMinimax(grid: CharArray): Int {
        val locations = grid.indicesOf(' ')
        var minimax = Int.MIN_VALUE
        var loc =0
        for( i in locations) {
            grid[i] = playerTurn.name[0]
            val temp = minimax(grid, playerTurn.next.value, true, done(grid.concatToString(), i, playerTurn, boxFill+1), boxFill+1)
            if(temp > minimax) {
                minimax = temp
                loc = i
            }
            grid[i] = ' '
        }
        return loc
    }

    /**
     * It calculates the optimization value of a player going to a particular location in a grid. You can read more about it on wikipedia
     */
    private fun minimax(grid: CharArray, player: PlayerType, doMin: Boolean, s: State, fill: Int): Int {

        if (s == State.NOT_DONE) {
            val locations = grid.indicesOf(' ')
            var minOrMax: Int? = null
            for( i in locations) {
                grid[i] = player.name[0]
                val temp = minimax(grid, player.next.value, !doMin,  done(grid.concatToString(), i, player, fill+1), fill+1)
                minOrMax = if(doMin) (minOrMax ?: Int.MAX_VALUE).coerceAtMost(temp) else (minOrMax ?: Int.MIN_VALUE).coerceAtLeast(temp)
                grid[i] = ' '

            }
            return minOrMax!!
        }
        else if(s == State.DRAW) {
            return 0
        }
        else if(s == playerTurn.state) {
            return 1
        }
        else return -1

    }

    /**
     *
     */
    private fun minimaxToTree(grid: CharArray, player: PlayerType, doMin: Boolean, s: State, computerTree: CustomTree<Pair<Int, Int?>>, root: CustomTree.Pos<Pair<Int, Int?>>, fill: Int): Int {

        if (s == State.NOT_DONE) {
            val locations = grid.indicesOf(' ')
            var minOrMax: Int? = null
            for( i in locations) {
                grid[i] = player.name[0]
                val pos = computerTree.addChild(root, Pair(i, null))
                val temp = minimaxToTree(grid, player.next.value, !doMin, done(grid.concatToString(), i, player, fill+1), computerTree, pos, fill+1)
                minOrMax = if(doMin) (minOrMax ?: Int.MAX_VALUE).coerceAtMost(temp) else (minOrMax ?: Int.MIN_VALUE).coerceAtLeast(temp)
                computerTree.setPos(pos, Pair(pos.element!!.first,temp))
                grid[i] = ' '

            }
            return minOrMax!!
        }
        else if(s == State.DRAW) {
            return 0
        }
        else if(s == playerTurn.state) {
            return 1
        }
        else return -1

    }

}