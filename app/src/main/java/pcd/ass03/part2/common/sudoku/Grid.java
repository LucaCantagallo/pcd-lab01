package pcd.ass03.part2.common.sudoku;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;
import de.sfuhrm.sudoku.Riddle;

import java.util.Optional;

public class Grid {

    private String gameMatrix;
    private String riddle;
    private String gamecode;

    private Cell[][] grid;

    public Grid(String gamecode) {
        GameMatrix gm;
        Riddle r;

        this.gamecode = gamecode;

        gm = Creator.createFull();


        r = Creator.createRiddle(gm); //

        grid = new Cell[9][9];
        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                grid[row][col] = new Cell(!r.getWritable(row,col)? Optional.of((int) gm.get(row,col)) : Optional.empty(), gm.get(row,col));
            }
        }
        GameCodeDatabase.addGameCode(gamecode, this);

        this.gameMatrix=gm.toString();
        this.riddle=gm.toString();

        System.out.println("__________________________________________PROVAPROVAPROVA");
        System.out.println("GM String");
        System.out.println(gm.toString());
        System.out.println("__________________________________________PROVAPROVAPROVA");
        System.out.println("R String");
        System.out.println(r.toString());

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

    public String getGamecode() {
        return this.gamecode;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getCell(int row, int col){
        return grid[row][col];
    }

    public String getGmMessage(){
        return gameMatrix;
    }

    public String getRMessage(){
        return riddle;
    }
}
