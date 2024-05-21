# Tic Tac Toe

This project is a tic tac toe program with 3 different modes: Two Player, Easy CPU (Random) and Hard CPU (Minimax algorithm). I made this for an AP Computer Science Final Project.

## How does the minimax algorithm work?

1. The algorithm evaluates all possible moves from the current game state.
2. For each possible move, it recursively evaluates all subsequent moves until it reaches a terminal state (win, lose, or draw).
3. The algorithm assigns a score to each terminal state: +10 for a win, -10 for a loss, and 0 for a draw. (Values could be opposite based on player turn/whether it is maximising or minimizing)
4. The algorithm assumes that both players play optimally and tries to maximize its own score while minimizing the opponent's score.
5. It backtracks and selects the move with the highest score at the top level.

## Applying minimax to Tic Tac Toe

In the context of Tic Tac Toe, the minimax algorithm can be used to determine the best move for the AI player. Here's a high-level overview of how it is be applied:

1. Generate a TTT board using Swing and AWT
2. After every move, start the algorithm which returns the best possible move
3. For each possible move, simulate the move and recursively apply the minimax algorithm to evaluate the resulting game state.
4. Assign a score to each terminal state: +10 for a win, -10 for a loss, and 0 for a draw.

By using the minimax algorithm, the AI player can make optimal moves in Tic Tac Toe and ensure the best possible outcome.
