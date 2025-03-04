package pcd.ass03.part2.common;

import de.sfuhrm.sudoku.GameMatrix;
import de.sfuhrm.sudoku.Riddle;
import de.sfuhrm.sudoku.Solver;
import de.sfuhrm.sudoku.Creator;

public class SudokuLauncherTry {
   public static void main(String[] args) {
       GameMatrix gm = Creator.createFull();
       Riddle r = Creator.createRiddle(gm);
       System.out.println(r);
   }
}
