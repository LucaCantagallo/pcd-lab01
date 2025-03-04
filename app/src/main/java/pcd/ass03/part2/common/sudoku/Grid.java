package pcd.ass03.part2.common.sudoku;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;
import de.sfuhrm.sudoku.Riddle;

import java.util.Optional;

public class Grid {

    private GameMatrix gm;
    private Riddle r;

    private Cell[][] grid;

    public Grid() {
        this.gm = Creator.createFull();

        this.r = Creator.createRiddle(gm); //

        grid = new Cell[9][9];
        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                grid[row][col] = new Cell(!r.getWritable(row,col)? Optional.of((int) gm.get(row,col)) : Optional.empty(), gm.get(row,col));
            }
        }
    }

    public int countEmpty(){
        int count = 0;
        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                if (!grid[row][col].isShowed()){
                    count++;
                }
            }
        }
        return count;
    }


    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getCell(int row, int col){
        return grid[row][col];
    }

    public GameMatrix getGm() {
        return gm;
    }

    public Riddle getR() {
        return r;
    }
}
