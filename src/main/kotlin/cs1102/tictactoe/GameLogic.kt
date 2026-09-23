package cs1102.tictactoe

import kotlin.random.Random
// I created the frontend with the help of the provided frontend and an LLM
val games : MutableMap<String,Game> = mutableMapOf( //predefined games
   "Empty" to Game(id = "Empty"),
    "Game1" to Game(id = "Game1", board = arrayOf( intArrayOf(1,0,1), intArrayOf(0,2,0), intArrayOf(0,0,2)) ),
    "Game2" to Game(id = "Game2", board = arrayOf( intArrayOf(1,2,1), intArrayOf(1,2,0), intArrayOf(0,0,2)) ),
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

fun placeMark(game: Game, row: Int, col: Int):BoardMarks{
    var lastMark: BoardMarks = game.humanSpace //base value will be changed

    when {
        row < 0 || row >= 3 || col < 0 || col >= 3 -> BoardMarks.EMPTY
        game.board[row][col] != BoardMarks.EMPTY.value -> BoardMarks.EMPTY
        else -> {
            if (game.isHumansTurn) {
                game.board[row][col] = game.humanSpace.value
                lastMark = game.humanSpace

            } else {
                game.board[row][col] = game.cpuSpace.value
                lastMark = game.cpuSpace
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
            //someone won
            println("Won")
            winStatus(game)
        } else if (isTie(game.board, 0, 0)) {
            println("Tie")
            game.status = GameStatus.TIE
        } else {
            if (game.isHumansTurn) {
                game.isHumansTurn = false
                cpuPlayer(game)

            } else {//toggles turn
                game.isHumansTurn = true
            }
            println("Game in Progress")
        }
    }

     return game


}












