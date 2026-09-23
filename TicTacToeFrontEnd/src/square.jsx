function Square({ value, onClick }) {
  const markClass =
    value === "X" ? "mark-x" : value === "O" ? "mark-o" : "";

  return (
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