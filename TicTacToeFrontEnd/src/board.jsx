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
      {board.map((rowArr, r) => (
        <div className="board-row" key={r}>
          {rowArr.map((value, c) => (
            <Square
              key={c}
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