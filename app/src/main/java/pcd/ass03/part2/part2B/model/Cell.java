package pcd.ass03.part2.part2B.model;

import java.awt.*;
import java.util.Optional;

public class Cell {

    private Optional<Integer> value;
    private int hiddenValue;
    private final boolean initialSet;
    private Color actualColor;

    public Cell(Optional<Integer> value, int hiddenValue) {
        this.value = value;
        this.hiddenValue = hiddenValue;
        this.initialSet = this.value.isPresent();
    }

    public Optional<Integer> getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = Optional.of(value);
    }

    public boolean isInitialSet(){
        return initialSet;
    }

    public int getHiddenValue() {
        return hiddenValue;
    }

    public Color getColor() {
        return actualColor;
    }

    public void setColor(Color actualColor) {
        this.actualColor = actualColor;
    }
}
