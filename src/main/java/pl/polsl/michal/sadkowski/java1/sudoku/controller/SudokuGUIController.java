// pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.java

package pl.polsl.michal.sadkowski.java1.sudoku.controller;

import pl.polsl.michal.sadkowski.java1.sudoku.model.SudokuGame;
import pl.polsl.michal.sadkowski.java1.sudoku.exceptions.SudokuException;

import java.util.Stack;
// Usunięto: import javax.swing.Timer;
// Usunięto: import java.awt.event.ActionEvent;

/**
 * Controller class for the Sudoku GUI application.
 * Manages game flow, user input, model updates, and view synchronization.
 *
 * @author Michał Sadkowski
 * @version 1.2 (SRP)
 */
public class SudokuGUIController {

    private final SudokuGame game;
    private final GUIUpdater gui;
    private final GameTimer gameTimer; 

    private final Stack<Move> moveHistory;


    public static class Move {
        final int row; 
        final int col;
        final String previousValue;

        Move(int row, int col, String previousValue) {
            this.row = row;
            this.col = col;
            this.previousValue = previousValue;
        }
    }

    /** Interfejs do komunikacji zwrotnej z Widokiem (Implementacja Obserwatora). */
    public interface GUIUpdater {
        void setTimerText(String text);
        void setCellValue(int row, int col, String value);
        void updateCellBorder(int row, int col);
        void clearBoardGUI();
        void showWinMessage(String time);
        void showInfoMessage(String message);
        void showErrorMessage(String message);
    }

    public SudokuGUIController(SudokuGame game, GUIUpdater gui) {
        this.game = game;
        this.gui = gui;
        this.moveHistory = new Stack<>();
        
        this.gameTimer = new GameTimer(gui); 
        this.gameTimer.start();
    }
    

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
    
    /** Obsługa kliknięcia lub fokusu na komórce. */
    public void handleCellClick(int row, int col) {
         gui.updateCellBorder(row, col);
    }
    
    /** Cofnięcie ostatniego ruchu. */
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
    
    /** Restartuje grę. */
    public void restartGame() {
        game.getBoard().clear();
        moveHistory.clear();
        gameTimer.stop();
        gameTimer.reset();
        gui.clearBoardGUI();
        gui.showInfoMessage("Plansza zresetowana.");
    }
    
    /** Rozpoczyna nową grę z wybranym poziomem trudności. */
    public void startNewGame(String selectedDifficulty) {
        gameTimer.stop();
        game.getBoard().clear();
        moveHistory.clear();
        gameTimer.reset();
        
        gui.clearBoardGUI();
        gameTimer.start();
        gui.showInfoMessage("Rozpoczynanie nowej gry - poziom: " + selectedDifficulty);
    }
    
    
    private void recordMove(int row, int col, String previousValue, String newValue) {
        if (!previousValue.equals(newValue)) {
            moveHistory.push(new Move(row, col, previousValue));
        }
    }

    private String getBoardCell(int row, int col) {
         try {
            int value = game.getBoard().getCell(row, col); 
            return value == 0 ? "" : String.valueOf(value);
        } catch (SudokuException e) {
            gui.showErrorMessage("Błąd odczytu komórki: " + e.getMessage());
            return "";
        }
    }

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
            gameTimer.stop();
            String time = gameTimer.getCurrentFormattedTime();
            gui.showWinMessage(time);
        }
    }
}