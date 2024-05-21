import javax.swing.JButton;
import java.awt.Color;
public class GameState {
    private char[][] board;
    private boolean gameOver;
    private int winner;
    private int turns;
    private char player1 = 'X';
    private char player2 = 'O';
    private char currentPlayer = player1;
    private int[] validMoves;
    public GameState() {
        board = new char[3][3];
        currentPlayer = 'X';
        gameOver = false;
        winner = 0;
        turns = 0;
        validMoves = new int[9];
    }
    public GameState(JButton[][] buttons) {
        this();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                String text = buttons[r][c].getText();
                if (text.equals("")) {
                    board[r][c] = 0;
                } else {
                    board[r][c] = text.charAt(0);
                    this.turns++;
                }
                
            }
        }
        this.currentPlayer = turns % 2 == 0 ? player1 : player2;
    }
    public GameState(GameState other) {
        board = new char[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c] = other.board[r][c];
            }
        }
        currentPlayer = other.currentPlayer;
        gameOver = other.gameOver;
        winner = other.winner;
        turns = other.turns;
        getValidMoves();
    }
    public GameState(GameState other, int move) {
        this(other);
        playMove(move / 3, move % 3);
    }
    public void setTurns(int turns) {
        this.turns = turns;
        this.currentPlayer = turns % 2 == 0 ? player1 : player2;
    }
    public int getTurns() {
        return turns;
    }
    public String getCurrentPlayer() {
        return currentPlayer + "";
    }
    public long getBitBoard() {
        long bitBoard = 0b0L;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                bitBoard <<= 1;
                if (board[r][c] == player1 || board[r][c] == player2) {
                    bitBoard |= 1;
                }
            }
        }
        return bitBoard;
    }
    public long getBitBoard(char player) {
        long bitBoard = 0b0L;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                bitBoard <<= 1;
                if (board[r][c] == player) {
                    bitBoard |= 1;
                }
            }
        }
        return bitBoard;
    }
    public GameState playMove(int move) {
        // create a new state where the current player plays the given move
        return new GameState(this, move);
    }
    private void playMove(int r, int c) {
        board[r][c] = currentPlayer;
        turns++;
        currentPlayer = this.turns % 2 == 0 ? player1 : player2;
    }
    public boolean isValidMove(int move) {
        // return whether the given move is valid
        getValidMoves();
        return validMoves[move] == 1;
    }
    public int[] getValidMoves() {
        // return an array of all valid moves
        long bitBoard = getBitBoard();
        int[] validMoves = new int[9];
        for (int i = 8; i >= 0; i--) {
            if ((bitBoard & (1 << i)) == 0) {
                validMoves[8-i] = 1;
            }
        }
        return validMoves;
    }
    public double evaluate() {
        // return the value of the current state
        // -10 if player1 wins, 10 if player2 wins, 0 otherwise
        // check rows
        for (int r = 0; r < 3; r++) {
            if (board[r][0] == board[r][1] && board[r][1] == board[r][2]) {
                if (board[r][0] == player1) {
                    return 10;
                } else if (board[r][0] == player2) {
                    return -10;
                }
            }
        }
        // check columns
        for (int c = 0; c < 3; c++) {
            if (board[0][c] == board[1][c] && board[1][c] == board[2][c]) {
                if (board[0][c] == player1) {
                    return 10;
                } else if (board[0][c] == player2) {
                    return -10;
                }
            }
        }
        // check diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == player1) {
                return 10;
            } else if (board[0][0] == player2) {
                return -10;
            }
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == player1) {
                return 10;
            } else if (board[0][2] == player2) {
                return -10;
            }
        }

        return 0;
    }
    public boolean isGameOver() {
        // return whether the game is over
        if (Math.abs(evaluate()) == (double)10 || turns == 9) {
            gameOver = true;
        }
        return gameOver;
    }
    public int getWinner() {
        // return the winner of the game
        double score = evaluate();
        if (score == 10) {
            winner = 1;
        } else if (score == -10) {
            winner = 2;
        } else {
            winner = 0;
        }
        return winner;
    }
    public char getWinnerName() {
        // return the name of the winner
        if (getWinner() == 1) {
            return player1;
        } else if (getWinner() == 2) {
            return player2;
        } else {
            return 0;
        }
    }
    public void setBoard(JButton[][] buttons) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                String text = board[r][c] + "";
                if (text.equals(((char)0)  + "")) {
                    text = "";
                }
                buttons[r][c].setText(text);
                buttons[r][c].setBackground(Color.darkGray);
                buttons[r][c].setForeground(Color.white);
            }
        }
    }
}
