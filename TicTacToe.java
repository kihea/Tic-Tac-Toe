
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe {
    private boolean initialized = false;
    int boardWidth = 600;
    int boardHeight = 650; // 50px for the text panel on top
    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton difficultyPanel = new JButton("Difficulty");
    GameState gameState = new GameState();
    JButton[][] board = new JButton[3][3];
    int isHard = -1;
    boolean gameOver = false;
    private long winningBitmasks[] = {
            0b100010001, 0b001010100, 
            0b100100100, 0b010010010, 0b001001001,
            0b111000000, 0b000111000, 0b000000111,
    }; // 8 possible ways to win
    JButton[][] getBoard() {
        return board;
    }
    void resetBoard() {
        if (!initialized) {
            return;
        }
        GameState newState = new GameState();
        gameState = newState;
        newState.setBoard(board);
        gameOver = false;
    }
    void playTicTacToe() {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        difficultyPanel.setLayout(new FlowLayout());
        difficultyPanel.setBackground(Color.darkGray);
        difficultyPanel.setForeground(Color.white);
        PopupMenu difficultyMenu = new PopupMenu("Difficulty");
        MenuItem twoPlayerButton = new MenuItem("Two Player");
        MenuItem easyButton = new MenuItem("Easy CPU");
        MenuItem hardButton = new MenuItem("Hard CPU");
        twoPlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isHard = -1;
                resetBoard();
            }
        });
        easyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isHard = 0;
                resetBoard();
            }
        });
        hardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isHard = 1;
                resetBoard();
            }
        });
        difficultyMenu.add(twoPlayerButton);
        difficultyMenu.add(easyButton);
        difficultyMenu.add(hardButton);
        difficultyPanel.add(difficultyMenu);
        difficultyPanel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                difficultyMenu.show(difficultyPanel, 0, 0);
            }
        });
        frame.add(difficultyPanel, BorderLayout.SOUTH);
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);
                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);
                // tile.setText(currentPlayer);
                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver) {
                            resetBoard();
                            return;
                        }
                        JButton tile = (JButton) e.getSource();
                        if (tile.getText() == "") { // Valid move
                            tile.setText(gameState.getCurrentPlayer());
                            gameState = new GameState(getBoard());
                            checkWinner();
                            if (!gameOver) {
                                gameState.setBoard(board);
                                textLabel.setText(gameState.getCurrentPlayer() + "'s turn.");
                                getCPUMove(isHard);
                            }
                        }
                    }
                });
            }
        }
        initialized = true;
    }
    void checkWinner() {
        if (gameState.isGameOver()) {
            gameOver = true;
            textLabel.setText("Game Over! " + gameState.getWinnerName() + " wins.");
            gameState.setBoard(board);
            if (gameState.getWinner() == 0) {
                setTie(board[1][1]);
                return;
            }
            for (int i = 0; i < 8; i++) {
                if ((gameState.getBitBoard(gameState.getWinnerName()) & winningBitmasks[i]) == winningBitmasks[i]) {
                    // shift each bit of the winning bitmask to find the winning combination of
                    // JButtons
                    for (int j = 8; j >= 0; j--) {
                        if ((winningBitmasks[i] & (1 << j)) != 0) {
                            int pos = 8 - j;
                            setWinner(board[pos / 3][pos % 3]);
                        }
                    }
                }
            }
        }
    }
    void setWinner(JButton tile) {
        tile.setForeground(Color.green);
        tile.setBackground(Color.gray);
        textLabel.setText(gameState.getWinnerName() + " is the winner!");
    }
    void setTie(JButton tile) {
        tile.setForeground(Color.orange);
        tile.setBackground(Color.gray);
        textLabel.setText("Tie!");
    }
    public int[] getValidMoves() {
        // return an array of all valid moves
        long bitBoard = gameState.getBitBoard();
        int[] validMoves = new int[9];
        for (int i = 8; i >= 0; i--) {
            if ((bitBoard & (1 << i)) == 0) {
                validMoves[8-i] = 1;
            }
        }
        return validMoves;
    }
    public int getCPUMove(int error) {
        if (error == -1) { // 2 player mode
            return -1;
        }
        // return the best move for the CPU
        // error == 0: random move
        // error >= 1: minmax algorithm
        if (gameState.isGameOver()){
            checkWinner();
            return -1;
        }
        int[] validMoves = getValidMoves();
        int bestMove = -1;
        while (true) {
            bestMove = (int) (Math.random() * 9);
            if (validMoves[bestMove] == 1) {
                break;
            }
        }
        if (error == 0) {
            while (true) {
                int move = (int) (Math.random() * 9);
                if (validMoves[move] == 1) {
                    bestMove = move;
                    break;
                }
            }
        } else {
            double bestScore = 1000;
            for (int i = 0; i < 9; i++) {
                if (validMoves[i] == 1) {
                    GameState newState = new GameState(gameState, i);
                    double score = minmax(newState, 0, true);
                    if (score < bestScore) {
                        bestScore = score;
                        bestMove = i;
                    }
                }
            }
        }
        if (bestMove == -1) {
            setTie(board[1][1]);
            gameOver = true;
        }
        doMove(bestMove);
        return bestMove;
    }
    public void doMove(int move) {
        gameState = new GameState(gameState, move);
        gameState.setBoard(board);
        checkWinner();
        if (!gameOver) {
            textLabel.setText(gameState.getCurrentPlayer() + "'s turn.");
        }
    }
    public double minmax(GameState board, int depth, boolean isMax) {
        // return the best score for the current board state
        if (board.isGameOver() || board.getTurns() == 9) {
            return board.evaluate();
        }
        if (isMax) {
            double bestScore = -1000;
            int[] validMoves = board.getValidMoves();
            for (int i = 0; i < 9; i++) {
                if (validMoves[i] == 1) {
                    GameState newState = new GameState(board, i);
                    double score = minmax(newState, depth + 1, false);
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            double bestScore = 1000;
            int[] validMoves = board.getValidMoves();
            for (int i = 0; i < 9; i++) {
                if (validMoves[i] == 1) {
                    GameState newState = new GameState(board, i);
                    double score = minmax(newState, depth + 1, true);
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }
}
