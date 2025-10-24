// pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.java

package pl.polsl.michal.sadkowski.java1.sudoku.controller;

import pl.polsl.michal.sadkowski.java1.sudoku.model.SudokuGame;
import pl.polsl.michal.sadkowski.java1.sudoku.exceptions.SudokuException;

import java.util.Stack;

/**
 * Controller class for the Sudoku GUI application.
 * Manages game flow, user input, model updates, and view synchronization.
 * It follows the Model-View-Controller (MVC) pattern.
 *
 * @author Michał Sadkowski
 * @version 1.2 (SRP)
 */
public class SudokuGUIController {

    private final SudokuGame game;
    private final GUIUpdater gui;
    private final GameTimer gameTimer; 

    private final Stack<Move> moveHistory;

    /**
     * Represents a single move made by the user, storing the cell coordinates and its previous value for undo functionality.
     */
    public static class Move {
        final int row; 
        final int col;
        final String previousValue;

        /**
         * Creates a new Move instance.
         *
         * @param row The 0-based row index (0-8).
         * @param col The 0-based column index (0-8).
         * @param previousValue The value in the cell before the move (empty string for 0).
         */
        Move(int row, int col, String previousValue) {
            this.row = row;
            this.col = col;
            this.previousValue = previousValue;
        }
    }

    /** * Interface for callback communication with the View (Observer pattern implementation).
     * This decouples the Controller from the concrete View implementation (like SudokuGUI).
     */
    public interface GUIUpdater {
        /**
         * Sets the text displayed by the timer.
         * @param text The new text for the timer display.
         */
        void setTimerText(String text);
        
        /**
         * Sets the value in a specific cell of the Sudoku board display.
         * @param row The 0-based row index (0-8).
         * @param col The 0-based column index (0-8).
         * @param value The value to set (empty string for empty cell).
         */
        void setCellValue(int row, int col, String value);
        
        /**
         * Updates the visual border/selection state of a specific cell.
         * @param row The 0-based row index (0-8).
         * @param col The 0-based column index (0-8).
         */
        void updateCellBorder(int row, int col);
        
        /** Clears the entire GUI representation of the board. */
        void clearBoardGUI();
        
        /**
         * Shows a message congratulating the user on winning the game.
         * @param time The total time taken to complete the Sudoku board.
         */
        void showWinMessage(String time);
        
        /**
         * Shows a general informational message to the user.
         * @param message The message to display.
         */
        void showInfoMessage(String message);
        
        /**
         * Shows an error message to the user.
         * @param message The error message to display.
         */
        void showErrorMessage(String message);
    }

    /**
     * Creates a new Sudoku GUI Controller instance.
     *
     * @param game The Sudoku game model.
     * @param gui The GUI updater interface for view communication.
     */
    public SudokuGUIController(SudokuGame game, GUIUpdater gui) {
        this.game = game;
        this.gui = gui;
        this.moveHistory = new Stack<>();
        
        this.gameTimer = new GameTimer(gui); 
        this.gameTimer.start();
    }
    
    /**
     * Handles user input (a digit or clearing the cell) for a specific cell.
     * Updates the model and view, and checks for a win condition.
     *
     * @param row The 0-based row index (0-8).
     * @param col The 0-based column index (0-8).
     * @param value The string value to input (a digit "1"-"9" or an empty string for 0).
     */
    public void handleCellInput(int row, int col, String value) {
        String previousValue = getBoardCell(row, col);
        
        if (value.isEmpty() || value.matches("[1-9]")) {
            if (!previousValue.equals(value)) {
                recordMove(row, col, previousValue, value);
            }
            
            try {
                int modelValue = value.isEmpty() ? 0 : Integer.parseInt(value);
                game.getBoard().setCell(row, col, modelValue);
            } catch (SudokuException e) {
                gui.showErrorMessage("Błąd Sudoku: " + e.getMessage());
                gui.setCellValue(row, col, previousValue); 
                return;
            }
            
            gui.setCellValue(row, col, value);
            
            checkWinCondition();
        } else {
            gui.setCellValue(row, col, previousValue);
        }
    }
    
    /**
     * Handles a click or focus event on a cell, updating the visual border/selection.
     *
     * @param row The 0-based row index (0-8).
     * @param col The 0-based column index (0-8).
     */
    public void handleCellClick(int row, int col) {
         gui.updateCellBorder(row, col);
    }
    
    /**
     * Undoes the last recorded move by restoring the previous cell value in the model and view.
     */
    public void undoLastMove() {
       if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.pop();
            
            try {
                 int modelValue = lastMove.previousValue.isEmpty() ? 0 : Integer.parseInt(lastMove.previousValue);
                 game.getBoard().setCell(lastMove.row, lastMove.col, modelValue); 
            } catch (SudokuException e) {
                 gui.showErrorMessage("Błąd podczas cofania ruchu: " + e.getMessage());
                 return;
            }
            
            gui.setCellValue(lastMove.row, lastMove.col, lastMove.previousValue);
            checkWinCondition();
        }
    }
    
    /**
     * Restarts the current game by clearing the board, move history, stopping and resetting the timer,
     * and clearing the GUI.
     */
    public void restartGame() {
        game.getBoard().clear();
        moveHistory.clear();
        gameTimer.stop();
        gameTimer.reset();
        gui.clearBoardGUI();
        gui.showInfoMessage("Plansza zresetowana.");
    }
    
    /**
     * Starts a new game with a selected difficulty.
     * Currently only resets the board, history, and timer, and starts the timer.
     *
     * @param selectedDifficulty The selected difficulty level (e.g., "Easy", "Medium", "Hard").
     */
    public void startNewGame(String selectedDifficulty) {
        gameTimer.stop();
        game.getBoard().clear();
        moveHistory.clear();
        gameTimer.reset();
        
        gui.clearBoardGUI();
        gameTimer.start();
        gui.showInfoMessage("Rozpoczynanie nowej gry - poziom: " + selectedDifficulty);
    }
    
    /**
     * Records a move to the history stack if the value is changing.
     *
     * @param row The 0-based row index.
     * @param col The 0-based column index.
     * @param previousValue The cell's value before the new input.
     * @param newValue The new value for the cell.
     */
    private void recordMove(int row, int col, String previousValue, String newValue) {
        if (!previousValue.equals(newValue)) {
            moveHistory.push(new Move(row, col, previousValue));
        }
    }

    /**
     * Gets the value of a cell from the game board.
     *
     * @param row The 0-based row index.
     * @param col The 0-based column index.
     * @return The cell value as a String ("1"-"9" or "" for 0), or an empty string on error.
     */
    private String getBoardCell(int row, int col) {
         try {
            int value = game.getBoard().getCell(row, col); 
            return value == 0 ? "" : String.valueOf(value);
        } catch (SudokuException e) {
            gui.showErrorMessage("Błąd odczytu komórki: " + e.getMessage());
            return "";
        }
    }

    /**
     * Checks if the board is completely filled. If it is, stops the timer and shows the win message.
     */
    private void checkWinCondition() {
        boolean isFull = true;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                 try {
                    if (game.getBoard().getCell(r, c) == 0) { 
                        isFull = false;
                        break;
                    }
                } catch (SudokuException e) {
                    gui.showErrorMessage("Błąd sprawdzania komórki: " + e.getMessage());
                    return;
                }
            }
            if (!isFull) break;
        }

        if (isFull) {
            // NOTE: Must also check if the board is correct (not implemented in SudokuGame.checkWin() yet)
            gameTimer.stop();
            String time = gameTimer.getCurrentFormattedTime();
            gui.showWinMessage(time);
        }
    }
}