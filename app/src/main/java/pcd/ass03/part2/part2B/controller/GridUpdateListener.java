package pcd.ass03.part2.part2B.controller;

import java.awt.*;

public interface GridUpdateListener {
    void onGridCreated();
    void onGridUpdated(String gamecode);
    void onCellSelected(String gamecode, int row, int col, Color color);
    void onCellUnselected(String gamecode, int row, int col);
}