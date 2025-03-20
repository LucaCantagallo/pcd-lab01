package pcd.ass03.part2.part2B.utils;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static final Map<String, Color> colorMap = new HashMap<>();

    static {
        colorMap.put("red", Color.RED);
        colorMap.put("green", Color.GREEN);
        colorMap.put("yellow", Color.YELLOW);
    }

    public static Color getColorByName(String colorName) {
        return colorMap.getOrDefault(colorName.toLowerCase(), Color.BLACK);
    }
}
