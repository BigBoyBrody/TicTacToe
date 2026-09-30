package cs1102.tictactoe

import kotlin.random.Random
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe



/*********************************************************************
I created the frontend with the help of the provided frontend and an LLM
 **********************************************************************/




val games : MutableMap<String,Game> = mutableMapOf( //predefined games
    "Empty" to Game(id = "Empty"),
    "Game1" to Game(id = "Game1", board = arrayOf( intArrayOf(1,0,1), intArrayOf(0,2,0), intArrayOf(0,0,2)) ),
    "Game2" to Game(id = "Game2", board = arrayOf( intArrayOf(1,2,1), intArrayOf(1,2,0), intArrayOf(0,0,2)) ),
    "Game3" to Game(id = "Game3", board = arrayOf( intArrayOf(1,0,1), intArrayOf(1,2,0), intArrayOf(0,2,2)) ),
    "Game4" to Game(id = "Game4", board = arrayOf( intArrayOf(0,2,1), intArrayOf(2,1,1), intArrayOf(2,1,2)) ),
    "Game5" to Game(id = "Game5", board = arrayOf( intArrayOf(1,0,0), intArrayOf(0,2,0), intArrayOf(1,0,0)) ),

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
    println(game.level)
    return game
}

fun checkRow(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        col == 3 ->  true
        board[row][col] != markToCheck.value -> false
        else -> checkRow(board, markToCheck, row, col+1)
    }
}

fun checkRows(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == 3 -> false
        checkRow(board, markToCheck, row, col)  -> true
        else -> checkRows(board, markToCheck, row +1, col)
    }
}
fun checkCol(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == 3 ->  true
        board[row][col] != markToCheck.value -> false
        else -> checkCol(board, markToCheck, row+1, col)
    }
}

fun checkCols(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        col == 3 -> false
        checkCol(board, markToCheck, row, col)  -> true
        else -> checkCols(board, markToCheck, row, col+1)
    }
}

fun checkNegDiagonal(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == 3 && col == 3-> true
        board[row][col] != markToCheck.value -> false
        else -> checkNegDiagonal(board, markToCheck, row+1, col+1)
    }
}
fun checkPosDiagonal(board : Array<IntArray>, markToCheck: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == -1 && col == 3-> true
        board[row][col] != markToCheck.value -> false
        else -> checkPosDiagonal(board, markToCheck, row-1, col+1)
    }
}
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

fun basicDepthSearch(game: Game, depth: Int): Game{
    if(depth == 1){
        var emptySpots = getEmptySpots(game,0,0)
        fun recurseEmptySpots(emptySpots: Array<IntArray>): Game{
            if(emptySpots.size == 1) {//end case if we are on the last one no other game worked return this one
                game.board[emptySpots[0][1]][emptySpots[0][1]] = game.cpuSpace.value
                return game
            }else {
                val newBoard = Array(game.board.size) { game.board[it].copyOf() }//deep copy of the board
                newBoard[emptySpots[0][0]][emptySpots[1][0]] = game.cpuSpace.value
                if (checkWin(newBoard, game.cpuSpace)) {
                    return game.copy(board = newBoard)//winning spot place it here
                } else {
                    return recurseEmptySpots(emptySpots.copyOfRange(1, emptySpots.size))
                }
            }
        }
        return recurseEmptySpots(emptySpots)
    }else{//else depth ==2
        return  game   }
}


/**
 * MiniMax algorithm implementation for TicTacToe
 *
 *
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
    return getNextStates(game,game.cpuSpace).maxBy{search(it,diffLevel) }
}

fun getEmptySpots(game: Game, row: Int,col: Int): Array<IntArray>{
    return when{
        col == 3 -> getEmptySpots(game, row+1, 0)
        row == 3 -> emptyArray()
        game.board[row][col] == BoardMarks.EMPTY.value -> arrayOf(intArrayOf(row,col)) + getEmptySpots(game,row,col+1)
        else -> getEmptySpots(game,row,col+1)
    }
}

fun getNextStates(game: Game, marker: BoardMarks): List<Game>{
    var emptySpaces = getEmptySpots(game,0,0)
    fun generateStates(): List<Game>{
        return when{
            emptySpaces.size == 0 ->emptyList()
            else -> {
                var newBoard = Array(game.board.size) { game.board[it].copyOf() }//deep copy of the board

                newBoard[emptySpaces[0][0]][emptySpaces[0][1]] = marker.value
                emptySpaces = emptySpaces.copyOfRange(1, emptySpaces.size) //updates for terminal check
                listOf(game.copy(board = newBoard, isHumansTurn = !game.isHumansTurn)) + generateStates() //returns and recurses
            }
        }
    }

    return generateStates()
}


fun trivialCases(board : Array<IntArray>, depth : Int): Boolean{
    return when{
        checkWin(board, BoardMarks.X) -> true
        checkWin(board, BoardMarks.O) -> true
        isTie(board,0,0) -> true
        depth == 0 -> true
        else -> false
    }
}

fun utilScore(board: Array<IntArray>) :Int{
    return when{
        checkWin(board, BoardMarks.X) -> -100
        checkWin(board, BoardMarks.O) -> 100
        isTie(board,0,0) -> 0
        else -> 0
    }
}

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

    val game5 = games["Game5"]
    if(game5 != null){
        game5.isHumansTurn = false
        println("minimaxxing")
        //  println( printBoard(game5.board,0,0))
        val newBoard = miniMax(game5,9).board
        println( printBoard(game5.board,0,0))
        println( printBoard(newBoard,0,0))


        // newBoard shouldBe arrayOf( intArrayOf(1,0,0), intArrayOf(2,2,0), intArrayOf(1,0,0))

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











