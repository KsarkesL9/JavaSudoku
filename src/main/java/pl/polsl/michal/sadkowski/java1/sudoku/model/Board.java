// pl.polsl.michal.sadkowski.java1.sudoku.model.Board.java

package pl.polsl.michal.sadkowski.java1.sudoku.model;

import pl.polsl.michal.sadkowski.java1.sudoku.exceptions.SudokuException;

/**
 * Class for Sudoku board 9x9.
 * This class store state and set/clear board.
 *
 * @author Michał Sadkowski
 * @version 1.2 (set to 0-indexing array)
 */
public class Board {
    /** The board size is 9x9. */
    private static final int N = 9;
    /** The 2D array for Sudoku grid. */
    private final int[][] grid;

    /**
     * Make new empty 9x9 Sudoku board.
     */
    public Board() {
        grid = new int[N][N];
    }

    /**
     * Set value in cell.
     *
     * @param row row index (0-8)
     * @param col column index (0-8)
     * @param value value to set (0-9, 0 is empty)
     * @throws SudokuException if row, column or value is bad range.
     */
    public void setCell(int row, int col, int value) throws SudokuException {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new SudokuException("Row/col out of range (0-8).");
        }
        if (value < 0 || value > 9) {
            throw new SudokuException("Value must be 0..9 (0 = empty).");
        }
        grid[row][col] = value;
    }

    /**
     * Get value from cell.
     *
     * @param row row index (0-8)
     * @param col column index (0-8)
     * @return the value in the cell (0-9)
     * @throws SudokuException if row or column is bad range.
     */
    public int getCell(int row, int col) throws SudokuException {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new SudokuException("Row/col out of range (0-8).");
        }
        return grid[row][col];
    }

    /**
     * Clear all board, set all cells to 0 (empty).
     */
    public void clear() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                grid[r][c] = 0;
            }
        }
    }

    /**
     * Make a string for board console display.
     *
     * @return good formatted string of the board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < N; r++) {
            if (r % 3 == 0 && r != 0) sb.append("------+-------+------\n");
            for (int c = 0; c < N; c++) {
                if (c % 3 == 0 && c != 0) sb.append("| ");
                int v = grid[r][c];
                sb.append(v == 0 ? ". " : (v + " "));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}