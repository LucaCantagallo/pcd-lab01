package pcd.ass03.part2.common.sudoku;

import java.util.Optional;

public class SudokuUtils {
    private Grid grid;

    public SudokuUtils(Grid grid) {
        this.grid = grid;
    }

    // Metodo per verificare se l'inserimento è corretto
    public boolean checkAndInsertValue(int row, int col, int insertedValue) {
        Cell cell = grid.getCell(row, col);

        // Confronta il valore inserito con il valore nascosto della cella
        if (cell.getHiddenValue() == insertedValue) {
            cell.setValue(Optional.of(insertedValue));
            grid.setValueCell(row,col,insertedValue);
            return true; // Valore corretto
        }
        //Da qui in futuro potremmo contare gli errori
        return false; // Valore errato
    }

    public void setFocus(int row, int col, boolean visited){
        grid.getCell(row,col).setVisited(visited);
        grid.getCell(row,col).setVisitedByMe(visited);
    }

    public boolean checkWinning(){
        return grid.countEmpty()==0;
    }
}
