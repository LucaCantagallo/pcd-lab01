package pcd.ass03.part2.common.view;

import pcd.ass03.part2.common.sudoku.Grid;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class Palette {
    private static final Map<MyColorList, Color> colors = new HashMap<>();

    static {
        colors.put(MyColorList.FOCUSCELL, new Color(0xE9F580));
        colors.put(MyColorList.WHITE, Color.WHITE);
        colors.put(MyColorList.RIGHTCELL, new Color(0x9BF580));
    }



    public static Color getColor(MyColorList color) {
        return colors.get(color);
    }
}
