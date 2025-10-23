package pl.polsl.michal.sadkowski.java1.sudoku.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Stack;

public class SudokuGUI extends JFrame {

    private BoardPanel boardPanel;
    private JLabel timerLabel;
    private Timer gameTimer;
    private int timeElapsed;
    private Stack<Move> moveHistory;


    private static class Move {
        int row;
        int col;
        String previousValue;

        Move(int row, int col, String previousValue) {
            this.row = row;
            this.col = col;
            this.previousValue = previousValue;
        }
    }


    public SudokuGUI() {
        super("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        moveHistory = new Stack<>();

        boardPanel = new BoardPanel(this);
        add(boardPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerLabel = new JLabel("Czas: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerPanel.add(timerLabel);
        controlPanel.add(timerPanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel numberButtonPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        for (int i = 1; i <= 9; i++) {
            JButton numButton = new JButton(String.valueOf(i));
            numButton.setFont(new Font("Arial", Font.BOLD, 16));
            numButton.setFocusable(false);
            int num = i;
            numButton.addActionListener(e -> boardPanel.setSelectedCellValue(String.valueOf(num)));
            numberButtonPanel.add(numButton);
        }
        JButton clearButton = new JButton("0");
        clearButton.setFont(new Font("Arial", Font.BOLD, 16));
        clearButton.setFocusable(false);
        clearButton.addActionListener(e -> boardPanel.setSelectedCellValue(""));
        numberButtonPanel.add(clearButton);

        controlPanel.add(numberButtonPanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton undoButton = new JButton("Cofnij");
        undoButton.setFocusable(false);
        undoButton.addActionListener(e -> undoLastMove());
        actionButtonPanel.add(undoButton);

        JButton restartButton = new JButton("Restart");
        restartButton.setFocusable(false);
        restartButton.addActionListener(e -> restartGame());
        actionButtonPanel.add(restartButton);

        JButton newGameButton = new JButton("Nowa gra");
        newGameButton.setFocusable(false);
        newGameButton.addActionListener(e -> startNewGame());
        actionButtonPanel.add(newGameButton);

        controlPanel.add(actionButtonPanel);

        add(controlPanel, BorderLayout.EAST);

        timeElapsed = 0;
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeElapsed++;
                int minutes = timeElapsed / 60;
                int seconds = timeElapsed % 60;
                timerLabel.setText(String.format("Czas: %02d:%02d", minutes, seconds));
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        startTimer();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_TYPED && boardPanel.hasSelection()) {
                    char keyChar = e.getKeyChar();
                    if (Character.isDigit(keyChar)) {
                        String value = String.valueOf(keyChar);
                        if(value.equals("0")) {
                           boardPanel.setSelectedCellValue("");
                        } else {
                           boardPanel.setSelectedCellValue(value);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void recordMove(int row, int col, String previousValue, String newValue) {
        if (!previousValue.equals(newValue)) {
            moveHistory.push(new Move(row, col, previousValue));
        }
    }


    private void undoLastMove() {
       if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.pop();
            boardPanel.setCellValue(lastMove.row, lastMove.col, lastMove.previousValue, false);
            checkWinCondition();
        }
    }

    private void restartGame() {
        boardPanel.clearBoard();
        moveHistory.clear();
        stopTimer();
        resetTimer();
         JOptionPane.showMessageDialog(this, "Plansza zresetowana.");
    }

    private void startNewGame() {
        stopTimer();
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
            boardPanel.clearBoard();
            moveHistory.clear();
            resetTimer();
            startTimer();
             JOptionPane.showMessageDialog(this, "Rozpoczynanie nowej gry - poziom: " + selectedDifficulty);

        } else {
             startTimer();
        }
    }

    private void startTimer() {
        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
    }

    private void stopTimer() {
        if (gameTimer.isRunning()) {
            gameTimer.stop();
        }
    }

    private void resetTimer() {
        timeElapsed = 0;
        timerLabel.setText("Czas: 00:00");
    }

    public void checkWinCondition() {
        if (boardPanel.isBoardFull()) {
            stopTimer();
            JOptionPane.showMessageDialog(this,
                    "Gratulacje! Ukończyłeś Sudoku w czasie " + String.format("%02d:%02d", timeElapsed / 60, timeElapsed % 60) + "!",
                    "Wygrana!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SudokuGUI();
            }
        });
    }
}