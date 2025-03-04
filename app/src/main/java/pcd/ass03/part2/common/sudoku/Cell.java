package pcd.ass03.part2.common.sudoku;

import java.util.Optional;

public class Cell {

    private Optional<Integer> value;
    private int hiddenValue;

    private Optional<Integer> valueInsered;
    private boolean isShowed;
    private boolean isVisited;
    private boolean isVisitedByMe;
    private CellListener listener;

    public Cell(Optional<Integer> value, int hiddenValue) {
        this.value = value;
        this.hiddenValue = hiddenValue;
        this.isShowed = !value.isEmpty();
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
        return isShowed;
    }

    public void setShowed(boolean showed) {
        isShowed = showed;
    }

    public void setValueInsered(int valueInsered) {
        if(!isShowed){
            this.valueInsered = Optional.of(valueInsered);
        }
    }

    public boolean trueValueChecker(){
        return this.valueInsered.isPresent() && this.valueInsered.get() == this.hiddenValue;
    }

    public boolean isVisitedByMe() {
        return isVisitedByMe;
    }

    public void setVisitedByMe(boolean visitedByMe) {
        isVisitedByMe = visitedByMe;
    }

    public void setListener(CellListener listener) {
        this.listener = listener;
    }
}
