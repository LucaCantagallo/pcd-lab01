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
        this.riddle=r.toString();

        System.out.println("__________________________________________PROVAPROVAPROVA");
        System.out.println("GM String");
        System.out.println(gm);
        System.out.println("__________________________________________PROVAPROVAPROVA");
        System.out.println("R String");
        System.out.println(r);

    }

    public Grid(String gamecode, String gm, String r) {
        this.gamecode = gamecode;
        this.gameMatrix=gm;
        this.riddle=gm;
        grid = new Cell[9][9];
        int index = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                char valueGm = (index < gm.length()) ? gm.charAt(index) : ' ';
                char valueR = (index < r.length()) ? gm.charAt(index++) : ' ';
                System.out.println(valueR);
                grid[row][col] = new Cell(!Character.isDigit(valueR) ? Optional.empty() : Optional.of(Character.getNumericValue(valueGm)), Character.getNumericValue(valueGm));
            }
        }
        GameCodeDatabase.addGameCode(gamecode, this);
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
