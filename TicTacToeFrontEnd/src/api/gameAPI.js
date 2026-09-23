const BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function parseOrThrow(response, label) {
  if (!response.ok) {
    throw new Error(`${label} failed: ${response.status} ${response.statusText}`);
  }
  return response.json();
}

export async function listGames() {
  const res = await fetch(`${BASE_URL}/games`);
  return parseOrThrow(res, "List games")
}

export async function getGame(gameId) {
  const res = await fetch(`${BASE_URL}/games/${gameId}`);
  return parseOrThrow(res, "Get game with id")
 
}

export async function createGame() {
  const res = await fetch(`${BASE_URL}/games`, { method: 'POST' });
  return parseOrThrow(res, "Create Game")
}

export async function playMove(gameId, row, col) {
  const res = await fetch(`${BASE_URL}/games/${gameId}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify([row, col]),
  });
    return parseOrThrow(res, "Create Game")
}