function Square({ value, onClick }) {
  return (
    <button className="square" onClick={onClick}>
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