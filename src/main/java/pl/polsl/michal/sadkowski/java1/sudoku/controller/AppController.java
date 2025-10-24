// pl.polsl.michal.sadkowski.java1.sudoku.controller.AppController.java

package pl.polsl.michal.sadkowski.java1.sudoku.controller;

import pl.polsl.michal.sadkowski.java1.sudoku.model.SudokuGame;
import pl.polsl.michal.sadkowski.java1.sudoku.exceptions.SudokuException;
import pl.polsl.michal.sadkowski.java1.sudoku.view.ConsoleView;

/**
 * Simple controller for the console-based Sudoku application.
 * It provides two modes:
 * <ul>
 * <li>If arguments are provided: execute a single command and exit.</li>
 * <li>If no arguments are provided: start an interactive REPL (Read-Eval-Print Loop).</li>
 * </ul>
 *
 * @author Michał Sadkowski
 * @version 1.2 (fit to 0-based indexing model)
 */
public class AppController {
    /** The Sudoku game state. */
    private final SudokuGame game = new SudokuGame("guest");
    /** The console view for showing output and taking input. */
    private final ConsoleView view = new ConsoleView();

    /**
     * Runs the application logic.
     *
     * @param args Command-line arguments to perform one task, or an empty/null array for REPL mode.
     */
    public void run(String[] args) {
        if (args != null && args.length > 0) {
            handleCommand(args);
        } else {
            repl();
        }
    }

    /**
     * Executes a single command based on command-line arguments.
     *
     * @param parts Array of command parts (command, row, column, value).
     * @throws SudokuException if a Sudoku rule is broken during a 'set' operation.
     * @throws Exception for any other unexpected error.
     */
    private void handleCommand(String[] parts) {
        String cmd = parts[0].toLowerCase();

        try {
            if (cmd.equals("print")) {
                view.display(game.getBoard());
                return;
            }
            if (cmd.equals("clear")) {
                game.getBoard().clear();
                view.display("Board cleaned.");
                return;
            }
            if (cmd.equals("get")) {
                if (parts.length < 3) {
                    view.display("How to use: get <row 1-9> <col 1-9>");
                    return;
                }
                Integer r = view.tryParse(parts[1]), c = view.tryParse(parts[2]);
                if (r == null || c == null || r < 1 || r > 9 || c < 1 || c > 9) {
                    view.display("Not a correct number argument for get (must be 1-9).");
                    return;
                }
                int val = game.getBoard().getCell(r - 1, c - 1); 
                view.display(String.format("Cell (%d,%d) = %d", r, c, val));
                return;
            }
            if (cmd.equals("set")) {
                if (parts.length < 4) {
                    view.display("How to use: set <row 1-9> <col 1-9> <value 0-9>");
                    return;
                }
                Integer r = view.tryParse(parts[1]), c = view.tryParse(parts[2]), v = view.tryParse(parts[3]);
                if (r == null || c == null || v == null || r < 1 || r > 9 || c < 1 || c > 9 || v < 0 || v > 9) {
                    view.display("Not a correct number value for set (r, c must be 1-9; v must be 0-9).");
                    return;
                }
                game.getBoard().setCell(r - 1, c - 1, v);
                view.display("OK");
                return;
            }
            view.display("Unknown command. Use: print, set, get, clear");

        } catch (SudokuException e) {
            view.display("Error: " + e.getMessage());
        } catch (Exception e) {
            view.display("An bad error happen: " + e.getMessage());
        }
    }

    /**
     * Starts the interactive Read-Eval-Print Loop (REPL) for console play.
     */
    private void repl() {
        view.display("Simple console sudoku editor 9x9");
        view.display("Commands: print | set r c v | get r c | clear | exit");

        while (true) {
            String line = view.prompt("cmd>");
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            String cmd = parts[0].toLowerCase();

            try {
                if (cmd.equals("exit")) {
                    view.display("Finish.");
                    return;
                } else if (cmd.equals("print")) {
                    view.display(game.getBoard());
                } else if (cmd.equals("clear")) {
                    game.getBoard().clear();
                    view.display("Board cleaned.");
                } else if (cmd.equals("get")) {
                    int r, c;
                    if (parts.length >= 3) {
                        r = Integer.parseInt(parts[1]);
                        c = Integer.parseInt(parts[2]);
                    } else {
                        r = view.askInt("Enter row 1-9:");
                        c = view.askInt("Enter column 1-9:");
                    }
                    if (r < 1 || r > 9 || c < 1 || c > 9) {
                        view.display("Error: Row/column must be 1-9.");
                        continue;
                    }
                    int val = game.getBoard().getCell(r - 1, c - 1); 
                    view.display(String.format("Cell (%d,%d) = %d", r, c, val));
                } else if (cmd.equals("set")) {
                    int r, c, v;
                    if (parts.length >= 4) {
                        r = Integer.parseInt(parts[1]);
                        c = Integer.parseInt(parts[2]);
                        v = Integer.parseInt(parts[3]);
                    } else {
                        r = view.askInt("Enter row 1-9:");
                        c = view.askInt("Enter column 1-9:");
                        v = view.askIntAllowZero("Enter value 0-9 (0 = empty):");
                    }
                    if (r < 1 || r > 9 || c < 1 || c > 9) {
                        view.display("Error: Row/column must be 1-9.");
                        continue;
                    }
                    game.getBoard().setCell(r - 1, c - 1, v);
                    view.display("OK");
                } else {
                    view.display("Unknown command. Use: print, set, get, clear, exit");
                }
            } catch (NumberFormatException e) {
                view.display("Error: Input is not a correct number value.");
            } catch (SudokuException e) {
                view.display("Error: " + e.getMessage());
            } catch (Exception e) {
                view.display("An bad error happen: " + e.getMessage());
            }
        }
    }
}