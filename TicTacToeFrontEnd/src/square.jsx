function Square({ value, onClick }) {
  const markClass =
    value === "X" ? "mark-x" : value === "O" ? "mark-o" : "";

  return (//simply makes a square object with its value and correct display
    <button className={`square ${markClass}`} onClick={onClick}>
      {value}
    </button>
  );
}

export default Square; 



{/*function Square() {
  return <button className="square">
  </button>;
}
 
export default Square; 
*/}