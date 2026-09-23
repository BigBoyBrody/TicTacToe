import { useState } from 'react'
import Board from "./Board";
import './App.css'

function App() {
  return (

    function handleBoardData(game) {
      
    }








    <div className="app"> {/* main div/wrapper */}
      <h1>Tic Tac Toe</h1>
 
      <div className="game-layout">
        <div className="select-game">
          <p>Select Game</p>
           <select 
              id="fruit-select"
              style={{ padding: '8px', borderRadius: '4px', width: '200px' }}
              >     
              <option value="apple">Apple</option>
              <option value="banana">Banana</option>
              <option value="orange">Orange</option>
              <option value="grape">Grape</option>
      </select>
          {/* dropdown goes here */}
        </div>
 
        <div className="board-area">
          <Board />
        </div>
 
        <div className="new-game">
          <button>New Game</button>
        </div>
 
        <div className="game-status">
          <p className="whosTurn">Player X's turn</p>
        </div>
      </div>
    </div>
  );
}

export default App
