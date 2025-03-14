package pcd.ass03.part2.common.sudoku;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;
import de.sfuhrm.sudoku.Riddle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Grid {

    private GameMatrix gmO;
    private Riddle rO;

    private String gameMatrix;
    private String riddle;
    private String gamecode;

    private Cell[][] grid;

    public Grid(String gamecode) {


        this.gamecode = gamecode;

        gmO = Creator.createFull();

        rO = Creator.createRiddle(gmO); //
        this.riddle = rO.toString();

        grid = new Cell[9][9];
        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                grid[row][col] = new Cell(!rO.getWritable(row,col)? Optional.of((int) gmO.get(row,col)) : Optional.empty(), gmO.get(row,col));
            }
        }

        this.gameMatrix=gmO.toString();
        this.riddle=rO.toString();

    }

    public Grid(String gamecode, String gm, String r){
        this.gamecode=gamecode;
        this.gameMatrix=gm;
        this.riddle=r;


        List<String> gmList = new ArrayList<>();
        List<String> rList = new ArrayList<>();

        // Itera attraverso ogni carattere della stringa
        for (int i = 0; i < gm.length(); i++) {
            char gmChar = gm.charAt(i);
            char rChar = r.charAt(i);
            // Aggiungi il carattere alla lista se non è uno spazio, una nuova linea, o SEPARATOR
            if (gmChar != ' ' && gmChar != '\n') {
                gmList.add(String.valueOf(gmChar));
            }
            if (rChar != ' ' && rChar != '\n') {
                rList.add(String.valueOf(rChar));
            }
        }


        List<Integer> gmMappedList = gmList.stream()
                .map(Integer::parseInt)  // Converte in Integer
                .collect(Collectors.toList());
        List<Optional<Integer>> rMappedList = new ArrayList<>();

        for (String s : rList) {
            if (s.matches("\\d+")) { // Controlla se la stringa è un numero
                rMappedList.add(Optional.of(Integer.parseInt(s)));
            } else {
                rMappedList.add(Optional.empty());
            }
        }

        int count=0;

        grid = new Cell[9][9];

        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                this.grid[row][col] = new Cell(rMappedList.get(count), gmMappedList.get(count));
                count++;
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

    public String getGamecode() {
        return this.gamecode;
    }

    public void setValueRiddle(int row, int col, byte value){
        rO.set(row, col, value);
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getCell(int row, int col){
        return grid[row][col];
    }

    public void setValueCell(int value, int row, int col){
        this.getCell(row, col).setValue(Optional.of(value));
    }

    public String getGameMatrixToString(){
        return gameMatrix;
    }

    public String getRiddleToString(){
        return this.riddle;
    }

    public void setRiddle(String riddle){
        this.riddle=riddle;
    }

}
