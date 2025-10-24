// pl.polsl.michal.sadkowski.java1.sudoku.view.SudokuGUI.java
package pl.polsl.michal.sadkowski.java1.sudoku.view;

import pl.polsl.michal.sadkowski.java1.sudoku.controller.SudokuGUIController;

import javax.swing.*;
import java.awt.*;
// FIX: Poprawne importy dla KeyboardFocusManager
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher; 
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionListener; // Dodano, ponieważ Swing go potrzebuje

public class SudokuGUI extends JFrame implements SudokuGUIController.GUIUpdater {

    private BoardPanel boardPanel;
    private JLabel timerLabel;
    private SudokuGUIController controller; 

    public SudokuGUI() {
        super("Sudoku");
        initUI();
    }
    
    /** Wstrzykuje Kontroler (Dependency Injection). */
    public void setController(SudokuGUIController controller) {
        this.controller = controller;
        // Przekazanie dalej do komponentu składowego
        this.boardPanel.setController(controller); 
    }
    
    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Inicjalizacja BoardPanel z null, bo Kontroler będzie wstrzyknięty później
        boardPanel = new BoardPanel(null); 
        add(boardPanel, BorderLayout.CENTER);

        // ... Budowa Panelu Kontrolnego ...

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerLabel = new JLabel("Czas: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerPanel.add(timerLabel);
        controlPanel.add(timerPanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Przyciski numeryczne
        JPanel numberButtonPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        for (int i = 1; i <= 9; i++) {
            JButton numButton = new JButton(String.valueOf(i));
            numButton.setFont(new Font("Arial", Font.BOLD, 16));
            numButton.setFocusable(false);
            int num = i;
            numButton.addActionListener(e -> {
                 int r = boardPanel.getSelectedRow();
                 int c = boardPanel.getSelectedCol();
                 // DELEGACJA: View -> Controller
                 if (controller != null) controller.handleCellInput(r, c, String.valueOf(num));
            });
            numberButtonPanel.add(numButton);
        }
        JButton clearButton = new JButton("0");
        clearButton.setFont(new Font("Arial", Font.BOLD, 16));
        clearButton.setFocusable(false);
        clearButton.addActionListener(e -> {
            int r = boardPanel.getSelectedRow();
            int c = boardPanel.getSelectedCol();
            // DELEGACJA: View -> Controller
            if (controller != null) controller.handleCellInput(r, c, "");
        });
        numberButtonPanel.add(clearButton);

        controlPanel.add(numberButtonPanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Przyciski akcji
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton undoButton = new JButton("Cofnij");
        undoButton.setFocusable(false);
        undoButton.addActionListener(e -> { if (controller != null) controller.undoLastMove(); }); 
        actionButtonPanel.add(undoButton);

        JButton restartButton = new JButton("Restart");
        restartButton.setFocusable(false);
        restartButton.addActionListener(e -> { if (controller != null) controller.restartGame(); });
        actionButtonPanel.add(restartButton);

        JButton newGameButton = new JButton("Nowa gra");
        newGameButton.setFocusable(false);
        newGameButton.addActionListener(e -> {
            String[] difficulties = {"Łatwy", "Średni", "Trudny"};
            String selectedDifficulty = (String) JOptionPane.showInputDialog(
                    this,
                    "Wybierz poziom trudności:",
                    "Nowa gra",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    difficulties,
                    difficulties[0]
            );

            if (selectedDifficulty != null) {
                if (controller != null) controller.startNewGame(selectedDifficulty);
            } else {
                 if (controller != null) controller.startTimer();
            }
        });
        actionButtonPanel.add(newGameButton);

        controlPanel.add(actionButtonPanel);

        add(controlPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Rejestracja globalnego nasłuchu klawiatury jest teraz poprawnie zaimportowana
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (controller == null) return false;
                // Logika wpisywania cyfr (Delegacja do Kontrolera)
                if (e.getID() == KeyEvent.KEY_TYPED && boardPanel.hasSelection()) {
                    char keyChar = e.getKeyChar();
                    if (Character.isDigit(keyChar)) {
                        String value = String.valueOf(keyChar);
                        int r = boardPanel.getSelectedRow();
                        int c = boardPanel.getSelectedCol();
                        
                        controller.handleCellInput(r, c, value); 
                        
                        return true;
                    }
                }
                return false;
            }
        });
    }

    // --- Implementacja interfejsu GUIUpdater (Widok wykonuje polecenia Kontrolera) ---
    
    @Override
    public void setTimerText(String text) {
        timerLabel.setText(text);
    }

    @Override
    public void setCellValue(int row, int col, String value) {
        // Kontroler mówi: Ustaw tę wartość WIDOCZNIE, View wykonuje.
        boardPanel.setCellValue(row, col, value); 
    }
    
    @Override
    public void updateCellBorder(int row, int col) {
        // Kontroler mówi: Zaktualizuj wizualny stan wybranej komórki.
        boardPanel.selectCell(row, col); 
    }
    
    @Override
    public void clearBoardGUI() {
        boardPanel.clearBoardGUI();
    }
    
    @Override
    public void showWinMessage(String time) {
        JOptionPane.showMessageDialog(this,
                    "Gratulacje! Ukończyłeś Sudoku w czasie " + time + "!",
                    "Wygrana!",
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    @Override
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Błąd aplikacji", JOptionPane.ERROR_MESSAGE);
    }
    
    // Usunięto starą metodę main
}