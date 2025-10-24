// pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.java
package pl.polsl.michal.sadkowski.java1.sudoku.controller;

import pl.polsl.michal.sadkowski.java1.sudoku.model.SudokuGame;
import pl.polsl.michal.sadkowski.java1.sudoku.exceptions.SudokuException;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.util.Stack;

public class SudokuGUIController {

    private final SudokuGame game;
    private final GUIUpdater gui; 

    // ---- Stan Gry (przeniesiony z Widoku) ----
    private final Stack<Move> moveHistory;
    private final Timer gameTimer;
    private int timeElapsed;
    // ---------------------------------------------

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
        
        this.timeElapsed = 0;
        this.gameTimer = new Timer(1000, this::handleTimerTick);
        startTimer();
    }
    
    // --- Metody obsługujące zdarzenia View ---

    /** Obsługa wprowadzenia nowej wartości przez użytkownika (z przycisku lub klawiatury). */
    public void handleCellInput(int row, int col, String value) {
        // ... (Logika kontrolera pozostaje bez zmian) ...
        String previousValue = getBoardCell(row, col);
        
        if (value.isEmpty() || value.matches("[1-9]")) {
            if (!previousValue.equals(value)) {
                recordMove(row, col, previousValue, value);
            }
            try {
                int modelValue = value.isEmpty() ? 0 : Integer.parseInt(value);
                // Komunikacja z Modelem
                game.getBoard().setCell(row + 1, col + 1, modelValue);
            } catch (SudokuException e) {
                // Obsługa błędu logiki Modelu
                gui.showErrorMessage("Błąd Sudoku: " + e.getMessage());
                gui.setCellValue(row, col, previousValue); 
                return;
            }
            
            // Komunikacja z Widokiem
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
                 game.getBoard().setCell(lastMove.row + 1, lastMove.col + 1, modelValue);
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
        stopTimer();
        resetTimer();
        gui.clearBoardGUI();
        gui.showInfoMessage("Plansza zresetowana.");
    }
    
    /** Rozpoczyna nową grę z wybranym poziomem trudności. */
    public void startNewGame(String selectedDifficulty) {
        stopTimer();
        game.getBoard().clear();
        moveHistory.clear();
        resetTimer();
        
        gui.clearBoardGUI();
        startTimer();
        gui.showInfoMessage("Rozpoczynanie nowej gry - poziom: " + selectedDifficulty);
    }
    
    // --- Metody zarządzające stanem gry ---

    private void handleTimerTick(ActionEvent e) {
        timeElapsed++;
        int minutes = timeElapsed / 60;
        int seconds = timeElapsed % 60;
        gui.setTimerText(String.format("Czas: %02d:%02d", minutes, seconds));
    }
    
    private void recordMove(int row, int col, String previousValue, String newValue) {
        if (!previousValue.equals(newValue)) {
            moveHistory.push(new Move(row, col, previousValue));
        }
    }

    private String getBoardCell(int row, int col) {
         try {
            int value = game.getBoard().getCell(row + 1, col + 1);
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
                    if (game.getBoard().getCell(r + 1, c + 1) == 0) {
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
            stopTimer();
            String time = String.format("%02d:%02d", timeElapsed / 60, timeElapsed % 60);
            gui.showWinMessage(time);
        }
    }

    public void startTimer() {
        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
    }

    public void stopTimer() {
        if (gameTimer.isRunning()) {
            gameTimer.stop();
        }
    }

    public void resetTimer() {
        timeElapsed = 0;
        gui.setTimerText("Czas: 00:00");
    }
}