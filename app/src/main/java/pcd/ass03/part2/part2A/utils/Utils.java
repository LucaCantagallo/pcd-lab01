package pcd.ass03.part2.part2A.utils;

import pcd.ass03.part2.part2A.model.Grid;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Utils {

    public static String generateRandomColor() {
        Random random = new Random();
        int red = random.nextInt(156)+100;
        int green = random.nextInt(156)+100;
        int blue = random.nextInt(156)+100;

        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public static Color convertStringToColor(String hex) {
        hex = hex.replace("#", "");

        int red = Integer.parseInt(hex.substring(0, 2), 16);
        int green = Integer.parseInt(hex.substring(2, 4), 16);
        int blue = Integer.parseInt(hex.substring(4, 6), 16);

        return new Color(red, green, blue);
    }

    public static Color getTransparentColor(Color myColor){
        return new Color(myColor.getRed(), myColor.getGreen(), myColor.getBlue(), 90);
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
