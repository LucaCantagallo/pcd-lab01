package pcd.ass03.part2.common.sudoku;

import java.util.Optional;

public class Cell {

    private Optional<Integer> value;
    private int hiddenValue;

    private boolean isVisited;
    private boolean isVisitedByMe;


    public Cell(Optional<Integer> value, int hiddenValue) {
        this.value = value;
        this.hiddenValue = hiddenValue;
        this.isVisited = false;
        this.isVisitedByMe = false;
    }

    public Optional<Integer> getValue() {
        return value;
    }

    public void setValue(Optional<Integer> value) {
        this.value = value;
    }

    public int getHiddenValue() {
        return hiddenValue;
    }

    public void setHiddenValue(int hiddenValue) {
        this.hiddenValue = hiddenValue;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean isShowed() {
        return value.isPresent();
    }

    public boolean isVisitedByMe() {
        return isVisitedByMe;
    }

    public void setVisitedByMe(boolean visitedByMe) {
        isVisitedByMe = visitedByMe;
    }

}
