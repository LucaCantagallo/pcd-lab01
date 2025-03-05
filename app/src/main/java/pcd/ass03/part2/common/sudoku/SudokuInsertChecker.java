package pcd.ass03.part2.common.sudoku;

import java.util.Optional;

public class SudokuInsertChecker {
    private Grid grid;

    public SudokuInsertChecker(Grid grid) {
        this.grid = grid;
    }

    // Metodo per verificare se l'inserimento è corretto
    public boolean checkInsert(int row, int col, int insertedValue) {
        Cell cell = grid.getCell(row, col);

        // Confronta il valore inserito con il valore nascosto della cella
        if (cell.getHiddenValue() == insertedValue) {
            cell.setValue(Optional.of(insertedValue));
            System.out.println("Corretto");
            System.out.println(grid.countEmpty());
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
