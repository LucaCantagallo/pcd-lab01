package pcd.ass03.part2.part2A.utils;

import pcd.ass03.part2.part2A.model.Grid;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static final Map<String, Color> colorMap = new HashMap<>();

    static {
        colorMap.put("green", Color.GREEN);
        colorMap.put("yellow", Color.YELLOW);
    }

    public static Color getColorByName(String colorName) {
        return colorMap.getOrDefault(colorName.toLowerCase(), Color.BLACK); // Default to black if not found
    }

    public static String toString(Grid grid) {
        StringBuilder sb = new StringBuilder();
        sb.append(grid.getGameCode());
        sb.append(" ");
        sb.append(grid.getGameMatrix());
        sb.append(",");
        sb.append(grid.getRiddle());
        return sb.toString();
    }

    public static Grid fromString(String message) {
        String[] parts = message.split(" ");
        String[] parts2 = parts[2].split(",");
        System.out.println("fromString: " + parts2[0]);
        System.out.println("p");
        return new Grid(Integer.parseInt(parts[0]), parts2[0], parts2[1], parts[1]);
    }

}
