package pcd.ass03.part2.part2B.utils;


import java.awt.*;
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
}
