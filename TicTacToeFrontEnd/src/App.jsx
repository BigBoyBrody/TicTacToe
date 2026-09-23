import { useState, useEffect } from 'react'
import Board from "./Board";
import { listGames, getGame, createGame, playMove } from "./api/gameAPI";
import './App.css'

function App() {
  const [games, setGames] = useState([]);
  const [gameId, setGameId] = useState(null);
  const [game, setGame] = useState(null); // { id, board, turn, winner }
//These constants hold variables that get updated by setGames, setGameID, and Set game
//They are updated when a property of them changes
// they start off as [] null null respectively

  //The functions below were created by AI (Claude)


  //Runs once, right after the component first mounts
  //  (empty [] dependency array means "run once, never again"). 
  // Fetches the list of games from the backend and stores it in games
  useEffect(() => {
    listGames().then(setGames).catch(console.error);
  }, []);

//Fetches a specific game by ID from the backend, then updates both gameId and game to match. 
// Used when the user picks an existing game from the dropdown.
  async function loadGame(id) {
    const g = await getGame(id);
    setGameId(id);
    setGame(g);
  }
//Calls the backend to create a new game, adds it to the games list, and makes it the active game
  async function handleNewGame() {
    const g = await createGame();
    setGames(prev => [...prev, g]);
    setGameId(g.id);
    setGame(g);
  }
//Runs when a board cell is clicked
//exits early if no gameId, there is a winnerm or the cell is already selected
// I have backend checks for these two as double security
  async function handleSquareClick(row, col) {
    if (!gameId || game?.winner || game.board[row][col]) return;
    const updated = await playMove(gameId, row, col);
    setGame(updated);
  }

  function getStatusMessage(game) {
  if (!game) return "Select or start a game";
  if (game.status === "PLAYING") return "Click a square to make a move";
  if (game.status === "TIE") return "Draw!";
  return `${game.winner} wins!`;
}

//The above functions were created by AI (Claude)

  return (
    <div className="app">
      <h1>Tic Tac Toe</h1>
      <div className="game-layout">
        <div className="select-game">
          <p>Select Game</p>
          <select
            style={{ padding: '8px', borderRadius: '4px', width: '200px' }}
            value={gameId || ""}
            onChange={(e) => loadGame(e.target.value)}
          >
            <option value="" disabled>Choose a game</option>
            {games.map(g => ( //this maps all the games to drop down menu
              <option key={g.id? g.id: g} value={g.id? g.id: g}>{g.id? g.id: g}</option>
            ))}
          </select>
        </div>

        <div className="board-area">
          <Board board={game?.board} onSquareClick={handleSquareClick} />
        </div>

        <div className="new-game">
          <button onClick={handleNewGame}>New Game</button>
        </div>

        <div className="game-status">
          <p className="whosTurn">
            {getStatusMessage(game)}
          </p>
        </div>
      </div>
    </div>
  );
}

export default App





// import { useState } from 'react'
// import Board from "./Board";
// import './App.css'

// function App() {
//   return (

//     <div className="app"> {/* main div/wrapper */}
//       <h1>Tic Tac Toe</h1>
 
//       <div className="game-layout">
//         <div className="select-game">
//           <p>Select Game</p>
//            <select 
//               id="fruit-select"
//               style={{ padding: '8px', borderRadius: '4px', width: '200px' }}
//               >     
//       </select>
//           {/* dropdown goes here */}
//         </div>
 
//         <div className="board-area">
//           <Board />
//         </div>
 
//         <div className="new-game">
//           <button>New Game</button>
//         </div>
 
//         <div className="game-status">
//           <p className="whosTurn">Player X's turn</p>
//         </div>
//       </div>
//     </div>
//   );
// }

// export default App