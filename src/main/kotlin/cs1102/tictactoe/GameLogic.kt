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

fun newGame() : Game{
    val game = Game(id = "Game" +  (games.size)) //since one predefined game state is empty we can just get the size without adding one
    games.put(game.id, game)
    println(games)
    return game
}

fun checkRow(board : Array<IntArray>, lastMark: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        col == 3 ->  true
        board[row][col] != lastMark.value -> false
        else -> checkRow(board, lastMark, row, col+1)
    }
}

fun checkRows(board : Array<IntArray>, lastMark: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == 3 -> false
        checkRow(board, lastMark, row, col)  -> true
        else -> checkRows(board, lastMark, row +1, col)
    }
}
fun checkCol(board : Array<IntArray>, lastMark: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == 3 ->  true
        board[row][col] != lastMark.value -> false
        else -> checkCol(board, lastMark, row+1, col)
    }
}

fun checkCols(board : Array<IntArray>, lastMark: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        col == 3 -> false
        checkCol(board, lastMark, row, col)  -> true
        else -> checkCols(board, lastMark, row, col+1)
    }
}

fun checkNegDiagonal(board : Array<IntArray>, lastMark: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == 3 && col == 3-> true
        board[row][col] != lastMark.value -> false
        else -> checkNegDiagonal(board, lastMark, row+1, col+1)
    }
}
fun checkPosDiagonal(board : Array<IntArray>, lastMark: BoardMarks, row: Int, col: Int): Boolean{
    return when{
        row == -1 && col == 3-> true
        board[row][col] != lastMark.value -> false
        else -> checkPosDiagonal(board, lastMark, row-1, col+1)
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

fun checkWin(board : Array<IntArray>, lastMark : BoardMarks ): Boolean{
    //check row
    return when{
        checkRows(board, lastMark,0,0) ->{println("Won by rows")
            true}
        checkCols(board, lastMark,0,0) -> {println("Won by cols")
            true}
        checkPosDiagonal(board, lastMark,2,0) -> {println("Won by pos diag")
            true}
        checkNegDiagonal(board, lastMark,0,0) -> {println("Won by neg diag")
            true}
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

//TODO
//find empty spots for each game
fun getNextStates(game: Game, marker: BoardMarks): List<Game>{
    fun getEmptySpots( row: Int,col: Int): Array<IntArray>{
        return when{
            col == 3 -> getEmptySpots( row+1, 0)
            row == 3 -> emptyArray()
            game.board[row][col] == BoardMarks.EMPTY.value -> arrayOf(intArrayOf(row,col)) + getEmptySpots(row,col+1)
            else -> getEmptySpots(row,col+1)
        }
    }

    var emptySpaces = getEmptySpots(0,0)
    fun generateStates(): List<Game>{
        return when{
            emptySpaces.size == 0 ->{
                println("REACHED EMPTY")
                emptyList()
            }
            else -> {
                var newBoard = Array(game.board.size) { game.board[it].copyOf() }

                newBoard[emptySpaces[0][0]][emptySpaces[0][1]] = marker.value

              //  println("Gen")
               // print( printBoard(game.board,0,0))
               // print(  printBoard(newBoard,0,0))
                emptySpaces = emptySpaces.copyOfRange(1, emptySpaces.size)

               // println(emptySpaces.contentDeepToString())

              //  emptySpaces = emptySpaces.drop(1).toTypedArray() //updates for terminal check
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

    val lastMark: BoardMarks =  when {
        row < 0 || row >= 3 || col < 0 || col >= 3 -> BoardMarks.EMPTY
        game.board[row][col] != BoardMarks.EMPTY.value -> BoardMarks.EMPTY
        else -> {
            if (game.isHumansTurn) {
                game.board[row][col] = game.humanSpace.value
                game.humanSpace

            } else {
                game.board[row][col] = game.cpuSpace.value
                game.cpuSpace
            }
        }

    }
    return lastMark
}

fun makeMove(game: Game, row : Int, col: Int) : Game{
    /**logic checks
     * Not out of bounds
     * Open spot
     */
    val lastMark: BoardMarks = placeMark(game, row, col) //places mark on board
    if (lastMark != BoardMarks.EMPTY && game.status == GameStatus.PLAYING) { //if it returns empty something went wrong(out of bound or player clicked a spot filled so we dont do anything)
        if (checkWin(game.board, lastMark)) {
            winStatus(game)
        } else if (isTie(game.board, 0, 0)) {
            println("Tie")
            game.status = GameStatus.TIE
        } else {
            if (game.isHumansTurn) {
                game.isHumansTurn = false
               // cpuPlayer(game)
                return miniMax(game,game.level)

            } else {//toggles turn
                game.isHumansTurn = true
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












