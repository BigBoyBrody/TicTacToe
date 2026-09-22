//package GameLogic
package cs1102.tictactoe

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException



@RestController
@CrossOrigin(origins = ["http://localhost:5173"])
class BackendController{

    //private val games: MutableMap<String, Game> = cs1102.tictactoe.games.toMutableMap()

    @GetMapping("api/games")
    fun getGames(): List<String>{
        return  games.keys.toList()
    }

    @GetMapping("api/games/{gameID}")
    fun getGame(@PathVariable gameID : String): Game? {
        val game = games[gameID]
        if(game != null) {
            return game
        }else{
            throw ResponseStatusException(HttpStatus.CONFLICT, "Game '${gameID}' Does Not Exist")
        }
    }

    @GetMapping("api/games/{game}/board")
    fun getboard(@PathVariable gameID : String): Array<IntArray>? {
        return  games[gameID]?.board
    }

    @PostMapping("api/games")
    @Synchronized
    fun createGame(): Game {
        return  newGame()
    }

    @PostMapping("api/games/{gameID}")
    @Synchronized
    fun playMove(@PathVariable gameID : String, @RequestBody move: Array<Int>): Game {
        val game = getGame(gameID)
        if(game != null) {
            return makeMove(game , move[0], move[1])
        }else{
            throw ResponseStatusException(HttpStatus.CONFLICT, "Game '${gameID}' Does Not Exist")
        }

    }


}
