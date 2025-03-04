package pcd.ass03.part2.common.sudoku;



import javax.swing.*;
import java.awt.*;

public class SudokuView extends JFrame implements CellListener {

    private Grid grid;

    public SudokuView(Grid grid) {
        this.grid = grid;
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(500, 500);
        add(createSudokuGrid(), BorderLayout.CENTER);
        setLocationRelativeTo(null);

        // Imposta il listener per tutte le celle
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cell = grid.getCell(row, col);
                cell.setListener(this);  // Assegna il listener
            }
        }
    }

    private JPanel createSudokuGrid() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(9, 9));

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cell = grid.getCell(row, col);
                GridView gridCell = new GridView(cell);
                gridPanel.add(gridCell);
            }
        }

        return gridPanel;
    }

    @Override
    public void onCellUpdated() {
        // Quando una cella è aggiornata, rinfresca la vista
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        Grid grid = new Grid();
        SudokuView view = new SudokuView(grid);
        view.setVisible(true);
    }
}
