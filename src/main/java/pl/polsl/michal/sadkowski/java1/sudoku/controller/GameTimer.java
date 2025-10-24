// pl.polsl.michal.sadkowski.java1.sudoku.controller.GameTimer.java

package pl.polsl.michal.sadkowski.java1.sudoku.controller;

import pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController.GUIUpdater;

import javax.swing.Timer;
import java.awt.event.ActionEvent;

/**
 * Timer logic.
 * Responsible only for time counting and updating the view.
 */
public class GameTimer {

    private final GUIUpdater gui;
    private final Timer gameTimer;
    private int timeElapsed;

    /**
     * Initializes the timer service.
     * @param gui The interface to communicate back to the view (to update the timer text).
     */
    public GameTimer(GUIUpdater gui) {
        this.gui = gui;
        this.timeElapsed = 0;
        this.gameTimer = new Timer(1000, this::handleTimerTick); 
    }
    
    /**
     * Handles the tick event every second.
     * @param e ActionEvent (ignored).
     */
    private void handleTimerTick(ActionEvent e) {
        timeElapsed++;
        int minutes = timeElapsed / 60;
        int seconds = timeElapsed % 60;
        gui.setTimerText(String.format("Czas: %02d:%02d", minutes, seconds));
    }

    /** Starts the timer if it is not already running. */
    public void start() {
        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
    }

    /** Stops the timer if it is running. */
    public void stop() {
        if (gameTimer.isRunning()) {
            gameTimer.stop();
        }
    }

    /** Resets the time count to zero and updates the view. */
    public void reset() {
        timeElapsed = 0;
        gui.setTimerText("Czas: 00:00");
    }

    /**
     * Gets the current time elapsed in minutes and seconds as a formatted string.
     * @return Formatted time string.
     */
    public String getCurrentFormattedTime() {
        int minutes = timeElapsed / 60;
        int seconds = timeElapsed % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}