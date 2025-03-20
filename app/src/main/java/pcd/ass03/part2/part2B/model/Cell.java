package pcd.ass03.part2.part2B.model;

import java.awt.*;
import java.io.Serializable;
import java.util.Optional;

public class Cell implements Serializable{

    private SerializableOptional<Integer> value;
    private int hiddenValue;
    private final boolean initialSet;
    private Color actualColor;

    public Cell(Optional<Integer> value, int hiddenValue) {
        this.value = new SerializableOptional<>(value);
        this.hiddenValue = hiddenValue;
        this.initialSet = this.value.getOptional().isPresent();
    }

    public Optional<Integer> getValue(){
        return value.getOptional();
    }

    public void setValue(int value){
        this.value = new SerializableOptional<>(Optional.of(value));
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
