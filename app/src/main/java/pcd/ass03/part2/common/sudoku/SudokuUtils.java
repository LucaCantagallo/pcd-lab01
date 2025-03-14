package pcd.ass03.part2.common.sudoku;

import pcd.ass03.part2.common.CommunicationProva.HandlerSingleSudoku;

import java.util.Optional;

public class SudokuUtils {

    // Metodo per verificare se l'inserimento è corretto
    public static boolean checkAndInsertValue(Grid sudokuGrid, int row, int col, int insertedValue) {
        Cell cell = sudokuGrid.getCell(row, col);

        // Confronta il valore inserito con il valore nascosto della cella
        if (cell.getHiddenValue() == insertedValue) {
            //cell.setValue(Optional.of(insertedValue));
            sudokuGrid.setValueCell(row,col,insertedValue);
            HandlerSingleSudoku.sendMessage(sudokuGrid);
            HandlerSingleSudoku.updateMessage(sudokuGrid);
            GameCodeDatabase.addGameCode(sudokuGrid.getGamecode(), sudokuGrid);
            return true; // Valore corretto
        }
        //Da qui in futuro potremmo contare gli errori
        return false; // Valore errato
    }

    public static void setFocus(Grid sudokuGrid, int row, int col, boolean visited){
        sudokuGrid.getCell(row,col).setVisited(visited);
        sudokuGrid.getCell(row,col).setVisitedByMe(visited);
    }

    public static boolean checkWinning(Grid sudokuGrid){
        return sudokuGrid.countEmpty()==0;
    }
}
