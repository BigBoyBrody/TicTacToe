import { useState, useEffect } from 'react'
import Board from "./Board";
import { listGames, getGame, createGame, playMove } from "./api/gameAPI";
import './App.css'

function App() {
  const [games, setGames] = useState([]);
  const [gameId, setGameId] = useState(null);
  const [game, setGame] = useState(null); // { id, board, turn, winner }

  useEffect(() => {
    listGames().then(setGames).catch(console.error);
  }, []);

  async function loadGame(id) {
    const g = await getGame(id);
    setGameId(id);
    setGame(g);
  }

  async function handleNewGame() {
    const g = await createGame();
    setGames(prev => [...prev, g]);
    setGameId(g.id);
    setGame(g);
  }

  async function handleSquareClick(row, col) {
    if (!gameId || game?.winner || game.board[row][col]) return;
    const updated = await playMove(gameId, row, col);
    setGame(updated);
    if(updated.status != "PLAYING"){
      if(updated.status == "TIE"){

    }
  }
  }

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
            {games.map(g => (
              <option key={g.id} value={g.id}>{g.id}</option>
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
            {game?.status != "PLAYING"
              ? game.status == "TIE" ? "Draw!" : `${game.winner} wins!`
              : game ? `Click board to play` : "Select or start a game"}
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