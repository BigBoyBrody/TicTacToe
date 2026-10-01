package cs1102.tictactoe

import kotlin.random.Random
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe



/*********************************************************************
I created the frontend with the help of the provided frontend and an LLM
 **********************************************************************/




val games : MutableMap<String,Game> = mutableMapOf( //predefined games
    "Empty" to Game(id = "Empty"),
    "Game1" to Game(id = "Game1", board = arrayOf( intArrayOf(1,0,0), intArrayOf(0,2,0), intArrayOf(0,0,0)), level = 2),//x goes top right for testing o must got top middle for block
    "Game2" to Game(id = "Game2", board = arrayOf( intArrayOf(1,0,2), intArrayOf(0,2,0), intArrayOf(0,0,1)), level = 1),//x goes middle right// o must got bottom left for win
    "Game3" to Game(id = "Game3", board = arrayOf( intArrayOf(0,0,0), intArrayOf(1,2,1), intArrayOf(0,2,0)), level = 1),//x goes bot right, goes top mid to win
    "Game4" to Game(id = "Game4", board = arrayOf( intArrayOf(1,0,1), intArrayOf(2,2,0), intArrayOf(0,0,0)), level = 1 ),//x goes bot right, o foes mid right to win
    "Game5" to Game(id = "Game5", board = arrayOf( intArrayOf(0,0,1), intArrayOf(2,0,1), intArrayOf(0,0,2)), level = 2),// x goes center, o blocks bottom left
    "Game6" to Game(id = "Game6", board = arrayOf( intArrayOf(1,0,0), intArrayOf(0,2,0), intArrayOf(0,0,0)), level = 2),// x goes bot left 0 blocks mid left
    "Game7" to Game(id = "Game7", board = arrayOf( intArrayOf(0,0,1), intArrayOf(0,1,2), intArrayOf(2,0,0)), level = 3),// x goes mid left
    "Game8"  to Game(id = "Game8",  board = arrayOf(intArrayOf(0,0,1), intArrayOf(0,1,2), intArrayOf(2,0,0)), level = 9), // x goes mid left
    "Game9"  to Game(id = "Game9",  board = arrayOf(intArrayOf(0,0,0), intArrayOf(1,2,1), intArrayOf(0,2,0)), level = 3), // x must block top middle
    "Game10" to Game(id = "Game10", board = arrayOf(intArrayOf(0,0,0), intArrayOf(0,0,2), intArrayOf(2,1,1)), level = 4), // x goes mid left
    "Game11" to Game(id = "Game11", board = arrayOf(intArrayOf(0,0,0), intArrayOf(0,0,0), intArrayOf(0,1,2)), level = 5), // x goes bot left
    "Game12" to Game(id = "Game12", board = arrayOf(intArrayOf(0,0,0), intArrayOf(0,0,0), intArrayOf(0,0,0)), level = 6), // x goes top left
    "Game13" to Game(id = "Game13", board = arrayOf(intArrayOf(0,0,0), intArrayOf(0,0,0), intArrayOf(0,0,0)), level = 7), // x goes center
    "Game14" to Game(id = "Game14", board = arrayOf(intArrayOf(0,0,0), intArrayOf(0,0,0), intArrayOf(0,0,0)), level = 8), // x goes top middle

    )

data class Game(
    val id: String,
    var board : Array<IntArray> = Array(3) { IntArray(3) {BoardMarks.EMPTY.value} },
    val level : Int = 0,
    var isHumansTurn : Boolean = true,
    var status : GameStatus = GameStatus.PLAYING,
    var winner : String? = null,
    val humanSpace : BoardMarks = BoardMarks.X,
    val cpuSpace : BoardMarks = BoardMarks.O,
)

enum class GameStatus{
    PLAYING,
    CPU_WIN,
    HUMAN_WIN,
    TIE
}

enum class BoardMarks(val value: Int){
    EMPTY(0),
    X(1),
    O(2)
}


//fun genBoard(board: Array<IntArray>, row: Int, col: Int) : Array<IntArray> {
//    return when{
//            row == 3 ->
//            col == 3 -> genBoard(board,row+1,0)
//            else -> {
//                board[row][col] = BoardMarks.EMPTY.value
//                genBoard(board, row, col+1)
//            }
//    }
//
//}

fun printBoard(board : Array<IntArray> ,row: Int, col: Int): String {
    return when{
        row == 3 -> return ""
        col == 3 -> "\n" + printBoard(board, row +1, 0)
        else -> board[row][col].toString() + " " + printBoard(board, row, col+1)
    }
}

fun newGame(difficultyLevel: Int) : Game{
    val game = Game(id = "Game" +  (games.size), level = difficultyLevel) //since one predefined game state is empty we can just get the size without adding one
    games.put(game.id, game)
    return game
}

/** Checks individual row
 * @param board the board to check
 * @param markToCheck the mark to check
 * @param row the starting row
 * @param col the starting col(default= 0)
 * @return true if win is reached
 */
fun checkRow(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        col == 3 ->  true
        board[row][col] != markToCheck.value -> false
        else -> checkRow(board, markToCheck, row, col+1)
    }
}
/** Checks all rows
 * @param board the board to check
 * @param markToCheck the mark to check
 * @param row the starting row(default= 0)
 * @param col the starting col(default= 0)
 * @return true if win is reached
 */
fun checkRows(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == 3 -> false
        checkRow(board, markToCheck, row, col)  -> true
        else -> checkRows(board, markToCheck, row +1, col)
    }
}
/** Checks individual col
 * @param board the board to check
 * @param markToCheck the mark to check
 * @param row the starting row(default= 0)
 * @param col the starting col
 * @return true if win is reached
 */
fun checkCol(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == 3 ->  true
        board[row][col] != markToCheck.value -> false
        else -> checkCol(board, markToCheck, row+1, col)
    }
}

/** Checks all cols
 * @param board the board to check
 * @param markToCheck the mark to check
 * @param row the starting row(default= 0)
 * @param col the starting col(default= 0)
 * @return true if win is reached
 */
fun checkCols(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        col == 3 -> false
        checkCol(board, markToCheck, row, col)  -> true
        else -> checkCols(board, markToCheck, row, col+1)
    }
}

/** Checks neg diagonal
 * @param board the board to check
 * @param markToCheck the mark to check
 * @param row the starting row(default= 0)
 * @param col the starting col(default= 0)
 * @return true if win is reached
 */
fun checkNegDiagonal(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == 3 && col == 3-> true
        board[row][col] != markToCheck.value -> false
        else -> checkNegDiagonal(board, markToCheck, row+1, col+1)
    }
}
/** Checks pos diagonal
 * @param board the board to check
 * @param markToCheck the mark to check
 * @param row the starting row(default= 0)
 * @param col the starting col(default= 0)
 * @return true if win is reached
 */
fun checkPosDiagonal(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == -1 && col == 3-> true
        board[row][col] != markToCheck.value -> false
        else -> checkPosDiagonal(board, markToCheck, row-1, col+1)
    }
}
/** Checks if every spot is full
 * @param board the board to check
 * @param row the starting row(default= 0)
 * @param col the starting col(default= 0)
 * @return true if every spot is full
 */
fun isTie(board : Array<IntArray>,row: Int,col: Int) : Boolean{
    return when{
        row == 3 -> true
        col == 3 -> isTie(board, row+1, 0)
        board[row][col] == BoardMarks.EMPTY.value -> false
        else -> {
            when{
                else -> isTie(board, row, col+1)
            }
        }
    }
}
/** Checks board for win
 * @param board the board to check
 * @param markToCheck the mark to check
 * @return true if win is reached
 */
fun checkWin(board : Array<IntArray>, markToCheck : BoardMarks ): Boolean{
    //check row
    return when{
        checkRows(board, markToCheck,0,0) -> true
        checkCols(board, markToCheck,0,0) -> true
        checkPosDiagonal(board, markToCheck,2,0) -> true
        checkNegDiagonal(board, markToCheck,0,0) -> true
        else -> false
    }
}
/** Sets status for win
 * @param game the game to set status
 */
fun winStatus(game: Game){
    if(game.isHumansTurn){
        game.status = GameStatus.HUMAN_WIN
        game.winner = "Human"
    }else{
        game.status = GameStatus.CPU_WIN
        game.winner = "Cpu"
    }
}

fun cpuPlayer(game: Game){
    var row = Random.nextInt(3) //0,1,2
    var col = Random.nextInt(3) //0,1,2

    if(game.board[row][col] == BoardMarks.EMPTY.value){ //if the board is full Its bad and will stakcOverflow make sure there is a check for this
        //THERE is a check, it checks for tie where all spots filled without win
        makeMove(game, row, col)
    }else
    {
        cpuPlayer(game)
    }

}

/**
 *  Picks random empty spot on
 * @param emptySpots the spots to randomly pick from
 * @return an IntArray of row,col
 */
fun pickRandomEmpty(emptySpots: Array<IntArray> ): IntArray{
    val rndSpot = Random.nextInt(emptySpots.size)
    return emptySpots[rndSpot]
}


/** Depth search for depths 1 and 2 (like minimax but fixed)
 *  @param game just the gamestate
 *  @param depth to go down (limit of 2)
 *  @return the game with the changed state
 */
fun basicDepthSearch(game: Game, depth: Int): Game{
    val emptySpots = getEmptySpots(game,0,0)
    if (emptySpots.isEmpty()) return game

    fun newBoard(spot: IntArray, marker: BoardMarks): Array<IntArray>{
        val newBoard = Array(game.board.size) { game.board[it].copyOf() }//deep copy of the board
        newBoard[spot[0]][spot[1]] = marker.value
        return newBoard
    }
    //I found out about firstOrNull while looking up getting first from filter and it works great, it gives me first winning spot by inputting the spot with the empty coordinates
    val winningSpot = emptySpots.firstOrNull({checkWin(newBoard(it,game.cpuSpace), game.cpuSpace)})
    return if(winningSpot != null){
        game.copy(board = newBoard(winningSpot, game.cpuSpace))
    }else{
        if(depth == 2){//we want to block the otehr win so check mins/check players board
            val blockingSpot = emptySpots.firstOrNull({checkWin(newBoard(it,game.humanSpace), game.humanSpace)})
            if(blockingSpot != null) {
                game.copy(board = newBoard(blockingSpot, game.cpuSpace))
            }else{
                game.copy(board = newBoard(pickRandomEmpty(emptySpots), game.cpuSpace))
            }
        }else{
            game.copy(board = newBoard(pickRandomEmpty(emptySpots), game.cpuSpace))
        }
    }
}


/**
 * MiniMax algorithm implementation for TicTacToe
 * @param game the game state with the board
 * @param diffLevel is the difficulty/max depth to search
 * @return returns the game with the cpus optimized move
 */
fun miniMax(game : Game, diffLevel: Int): Game{
    fun search(gameState: Game, depth: Int): Int{
        return when{
            trivialCases(gameState.board,depth) ->utilScore(gameState.board)
            gameState.isHumansTurn -> {
                getNextStates(gameState,gameState.humanSpace).minOf{search(it,depth-1)}
            }
            else -> {
                (getNextStates(gameState,gameState.cpuSpace).maxOf{search(it,depth-1)})
            }
        }
    }
    return getNextStates(game,game.cpuSpace).maxBy{search(it,diffLevel-1) }
}

/** Gets all empty spots in board
 * @param game the game to find all the empty spots in the boar\d
 * @param row the row to start search (default use enter 0)
 * @param col the col to start search (default use enter 0)
 * @return returns an array of intarray that has row,col of empty spots on board
 */
fun getEmptySpots(game: Game, row: Int,col: Int): Array<IntArray>{
    return when{
        col == 3 -> getEmptySpots(game, row+1, 0)
        row == 3 -> emptyArray()
        game.board[row][col] == BoardMarks.EMPTY.value -> arrayOf(intArrayOf(row,col)) + getEmptySpots(game,row,col+1)
        else -> getEmptySpots(game,row,col+1)
    }
}

/** Gets all possible states for one depth by randomly picking a spot to pick
 *  @param game the game to check
 *  @param marker the marker to randomly place
 *  @return A list of games with the boards mutated to match every possible gameState
 */
fun getNextStates(game: Game, marker: BoardMarks): List<Game>{
    var emptySpaces = getEmptySpots(game,0,0)
    fun generateStates(): List<Game>{
        return when{
            emptySpaces.size == 0 ->emptyList()
            else -> {
                var newBoard = Array(game.board.size) { game.board[it].copyOf() }//deep copy of the board
                val randomSpace = pickRandomEmpty(emptySpaces)
                newBoard[randomSpace[0]][randomSpace[1]] = marker.value
                //emptySpaces = emptySpaces.copyOfRange(1, emptySpaces.size) //updates for terminal check
                emptySpaces = emptySpaces.filter {!it.contentEquals(randomSpace)  }.toTypedArray()//returns every thing that dosent equal the row,col so filtering it out
                listOf(game.copy(board = newBoard, isHumansTurn = !game.isHumansTurn)) + generateStates() //returns and recurses
            }
        }
    }

    return generateStates()
}

/** checks for end of game by win or tie or if reached depth for minimax
 * @param board the board to check for states
 * @param depth the depth we are at
 * @return true if game ends or depth == 0
 */
fun trivialCases(board : Array<IntArray>, depth : Int): Boolean{
    return when{
        checkWin(board, BoardMarks.X) -> true
        checkWin(board, BoardMarks.O) -> true
        isTie(board,0,0) -> true
        depth == 0 -> true
        else -> false
    }
}

/** Calculates score for minimax
 * @param board the board to calc score
 * @return the util score
 * If player wins -100
 * if cpu wins + 100
 * else 0
 */
fun utilScore(board: Array<IntArray>) :Int{
    return when{
        checkWin(board, BoardMarks.X) -> -100
        checkWin(board, BoardMarks.O) -> 100
        isTie(board,0,0) -> 0
        else -> 0
    }
}

/**
 * Places mark on the board
 * @param game to put marker on
 * @param row row to put marker on
 * @param col col to put marker on
 * @return the boardmark that was placed
 */
fun placeMark(game: Game, row: Int, col: Int):BoardMarks{
    //var lastMark: BoardMarks = BoardMarks.EMPTY //base value will be changed
    //We only have to check if player inputted valid spot
    val lastMark: BoardMarks =  when {
        row < 0 || row >= 3 || col < 0 || col >= 3 -> BoardMarks.EMPTY
        game.board[row][col] != BoardMarks.EMPTY.value -> BoardMarks.EMPTY
        else -> { //only the human/player calls this function
            game.board[row][col] = game.humanSpace.value
            game.humanSpace
        }

    }
    return lastMark
}

/** Helper to check status win
 * @param game game to check
 * @param marker the space marker to check
 * @return treu if game is ended
 */
fun checkStatus(game: Game, marker: BoardMarks):Boolean{
    if(checkWin(game.board, marker)){
        winStatus(game)
        return true
    }
    else if(isTie(game.board,0,0)){
        game.status = GameStatus.TIE
        return true
    }else
    {
        return false
    }


}

/**
 * Makes move from player and then makes cpu move
 * @param game the game to make move
 * @param row the row to place human place
 * @param col the col to place human place
 * @return the game with altered board
 */
fun makeMove(game: Game, row : Int, col: Int) : Game{
    //The frontend calls this function, meaning the player clicked this spot
    val lastMark: BoardMarks = placeMark(game, row, col) //places mark on board with players mark
    if (lastMark != BoardMarks.EMPTY && game.status == GameStatus.PLAYING) { //if it returns empty something went wrong(out of bound or player clicked a spot filled so we dont do anything)
        if(!checkStatus(game,game.humanSpace)) {//this checks if game ended by checking if human won or tie, if neither we continue
            //The human just went so we want to play the cpus turn
            game.isHumansTurn = false//now cpus turn

            val cpusGame = miniMax(game,game.level)// this is the best calculated move based off of algo + diff level
            game.board = cpusGame.board//mutates board to reflect it on backend

            if(!checkStatus(game,game.cpuSpace)) {//now we check if its a win for cpu or tie and then move one
                game.isHumansTurn = true//back to humans turn
            }
        }
    }
    return game
}

fun main(){

    //Tests for level 1
    val game2 = games["Game2"]
    if(game2 != null){//DIAG WIN
        makeMove(game2,1,2)//x goes middle right
        //O should go bottom left
        game2.board[2][0] shouldBe  game2.cpuSpace.value
        game2.status shouldBe GameStatus.CPU_WIN
    }

    val game3 = games["Game3"]
    if(game3 != null){// COL WIN
        makeMove(game3,2,2)//x goes bot right
        //O should go top mid
        game3.board[0][1] shouldBe  game3.cpuSpace.value
        game3.status shouldBe GameStatus.CPU_WIN
    }

    val game4 = games["Game4"]
    if(game4!= null){ // ROW WIN
        makeMove(game4,2,2)//x goes bot right
        //O should go mid right
        game4.board[1][2] shouldBe game4.cpuSpace.value
        game4.status shouldBe GameStatus.CPU_WIN
    }



    //test for level 2
    val game5 = games["Game5"]
    if(game5 != null){ // DIAG BLOCK
        makeMove(game5,1,1)//x goes center
        //O should go bottom left
        game5.board[2][0] shouldBe  game5.cpuSpace.value
        game5.status shouldBe GameStatus.PLAYING
    }

    val game6 = games["Game6"]
    if(game6 != null){ //COL BLOCK
        makeMove(game6,2,0)//x bott left
        //O blocks mid left
        game6.board[1][0] shouldBe  game6.cpuSpace.value
        game6.status shouldBe GameStatus.PLAYING
    }

    val game1 = games["Game1"]
    if(game1!= null){ //ROW BLOCK
        makeMove(game1,0,2)//x goes top right
        //O should go top mid
        game1.board[0][1] shouldBe game1.cpuSpace.value
        game1.status shouldBe GameStatus.PLAYING
    }



    //test for level 3
    val game7 = games["Game7"]
    if(game7 != null){
        makeMove(game7,1,0)//mid left
        //cpu should go bot right
        arrayOf(arrayOf( intArrayOf(0,0,1), intArrayOf(1,1,2), intArrayOf(2,0,2)),arrayOf( intArrayOf(0,0,1), intArrayOf(1,1,2), intArrayOf(2,2,0)),arrayOf( intArrayOf(2,0,1), intArrayOf(1,1,2), intArrayOf(2,0,0)),arrayOf( intArrayOf(0,2,1), intArrayOf(1,1,2), intArrayOf(2,0,0))).any { it.contentDeepEquals( game7.board ) } shouldBe true
       // game7.board[2][2] shouldBe  game7.cpuSpace.value

    }

    val game8 = games["Game8"]
    if(game8 != null){
        makeMove(game8,1,0)//mid left
        //cpu can go anywhere left: all four replies draw
        arrayOf(
            arrayOf(intArrayOf(2,0,1), intArrayOf(1,1,2), intArrayOf(2,0,0)),
            arrayOf(intArrayOf(0,2,1), intArrayOf(1,1,2), intArrayOf(2,0,0)),
            arrayOf(intArrayOf(0,0,1), intArrayOf(1,1,2), intArrayOf(2,2,0)),
            arrayOf(intArrayOf(0,0,1), intArrayOf(1,1,2), intArrayOf(2,0,2))
        ).any { it.contentDeepEquals(game8.board) } shouldBe true
    }

    val game9 = games["Game9"]
    if(game9 != null){
        makeMove(game9,0,1)//top middle (blocks column)
        //cpu should fork: bot left or bot right (win in 3 plies)
        arrayOf(
            arrayOf(intArrayOf(0,1,0), intArrayOf(1,2,1), intArrayOf(2,2,0)),
            arrayOf(intArrayOf(0,1,0), intArrayOf(1,2,1), intArrayOf(0,2,2))
        ).any { it.contentDeepEquals(game9.board) } shouldBe true
    }

    val game10 = games["Game10"]
    if(game10 != null){
        makeMove(game10,1,0)//mid left
        //cpu must avoid top right (lets x fork at the center): top left, top middle, or center
        arrayOf(
            arrayOf(intArrayOf(2,0,0), intArrayOf(1,0,2), intArrayOf(2,1,1)),
            arrayOf(intArrayOf(0,2,0), intArrayOf(1,0,2), intArrayOf(2,1,1)),
            arrayOf(intArrayOf(0,0,0), intArrayOf(1,2,2), intArrayOf(2,1,1))
        ).any { it.contentDeepEquals(game10.board) } shouldBe true
    }

    val game11 = games["Game11"]
    if(game11 != null){
        makeMove(game11,2,0)//bot left
        //cpu has a forced win in 3 moves: top right or mid right (level 4 can't see it)
        arrayOf(
            arrayOf(intArrayOf(0,0,2), intArrayOf(0,0,0), intArrayOf(1,1,2)),
            arrayOf(intArrayOf(0,0,0), intArrayOf(0,0,2), intArrayOf(1,1,2))
        ).any { it.contentDeepEquals(game11.board) } shouldBe true
    }

    val game12 = games["Game12"]
    if(game12 != null){
        makeMove(game12,0,0)//top left corner
        //cpu should take the center, the only drawing reply
        arrayOf(
            arrayOf(intArrayOf(1,0,0), intArrayOf(0,2,0), intArrayOf(0,0,0))
        ).any { it.contentDeepEquals(game12.board) } shouldBe true
    }

    val game13 = games["Game13"]
    if(game13 != null){
        makeMove(game13,1,1)//center
        //cpu should take any corner
        arrayOf(
            arrayOf(intArrayOf(2,0,0), intArrayOf(0,1,0), intArrayOf(0,0,0)),
            arrayOf(intArrayOf(0,0,2), intArrayOf(0,1,0), intArrayOf(0,0,0)),
            arrayOf(intArrayOf(0,0,0), intArrayOf(0,1,0), intArrayOf(2,0,0)),
            arrayOf(intArrayOf(0,0,0), intArrayOf(0,1,0), intArrayOf(0,0,2))
        ).any { it.contentDeepEquals(game13.board) } shouldBe true
    }

    val game14 = games["Game14"]
    if(game14 != null){
        makeMove(game14,0,1)//top middle edge
        //cpu should go top left, top right, center, or bot middle
        arrayOf(
            arrayOf(intArrayOf(2,1,0), intArrayOf(0,0,0), intArrayOf(0,0,0)),
            arrayOf(intArrayOf(0,1,2), intArrayOf(0,0,0), intArrayOf(0,0,0)),
            arrayOf(intArrayOf(0,1,0), intArrayOf(0,2,0), intArrayOf(0,0,0)),
            arrayOf(intArrayOf(0,1,0), intArrayOf(0,0,0), intArrayOf(0,2,0))
        ).any { it.contentDeepEquals(game14.board) } shouldBe true
    }




//arrayOf( intArrayOf(1,0,0), intArrayOf(0,2,0), intArrayOf(1,0,0)) )





//    val game1 = games["Game1"]
//    if(game1 is Game) {
//        placeMark(game1, 3, 4) shouldBe BoardMarks.EMPTY
//        placeMark(game1, 0, 0) shouldBe BoardMarks.EMPTY
//        makeMove(game1, 3, 4) shouldBe game1
//        makeMove(game1, 0, 0) shouldBe game1
//    }
//
//    val game3 = games["Game3"]
//    if(game3 is Game) {
//
//        val copy = game3.copy()
//        makeMove(copy, 0, 1)
//        copy.status shouldBe GameStatus.HUMAN_WIN
//
//        val copy2 = game3.copy()
//
//        copy2.board[0][1] = 0
//
//        copy2.isHumansTurn = false
//        makeMove(copy2, 0, 1)
//        copy2.status shouldBe GameStatus.CPU_WIN
//        copy2.board[0][1] = 0
//    }
//
//    val game4 = games["Game4"]
//    if(game4 is Game) {
//        val copy = game4.copy()
//        makeMove(copy, 0, 0)
//        copy.status shouldBe GameStatus.TIE
//    }



}











