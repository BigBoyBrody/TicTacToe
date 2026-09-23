import Square from "./square";

const CELL_DISPLAY = {
  0: "",   // empty
  1: "X",
  2: "O",
};

function Board({ board, onSquareClick }) {
  if (!board) return <div className="board">No game loaded</div>;

  return (
    <div className="board">
      {board.map((rowArr, r) => (//.map gives us the row
        <div className="board-row" key={r}> {/* the key is the board-row */}
          {rowArr.map((value, c) => ( //.map gives us the col
            <Square
              key={c}//assigns key for that square which is col
              value={CELL_DISPLAY[value]}
              onClick={() => onSquareClick(r, c)}
            />
          ))}
        </div>
      ))}
    </div>
  );
}

export default Board;

{/*import Square from "./square";
 
function Board() {
  return (
    <div className="board">
      <div className="board-row">
        <Square />
        <Square />
        <Square />
      </div>
      <div className="board-row">
        <Square />
        <Square />
        <Square />
      </div>
      <div className="board-row">
        <Square />
        <Square />
        <Square />
      </div>
    </div>
  );
}
 
export default Board;
 */}