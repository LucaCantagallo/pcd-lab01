package pcd.ass03.part2.common.view;

import pcd.ass03.part2.common.sudoku.Cell;
import pcd.ass03.part2.common.sudoku.Grid;
import pcd.ass03.part2.common.sudoku.SudokuUtils;

import javax.swing.*;

public class HandlerSingleSudokuView {
    private static SudokuView sudokuView; // Aggiungiamo un riferimento alla GUI


    public static void setSudokuView(SudokuView view) {
        sudokuView = view;
    }

    public static void updateGridUI(Grid sudokuGrid, JTextField[][] textFields){
        if (sudokuView != null){
            SwingUtilities.invokeLater(() -> {  // Esegui nel thread della GUI
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        Cell cell = sudokuGrid.getCell(row, col);
                        if (!cell.isShowed()) {
                            textFields[row][col].setText(cell.getValue().map(String::valueOf).orElse(""));
                        }
                    }
                }
                sudokuView.revalidate();
                sudokuView.repaint();
            });
        }
    }
}
