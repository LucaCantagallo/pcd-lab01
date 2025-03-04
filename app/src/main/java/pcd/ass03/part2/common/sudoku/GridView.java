package pcd.ass03.part2.common.sudoku;

import pcd.ass03.part2.common.sudoku.Cell;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GridView extends JPanel {

    private Cell cell;

    public GridView(Cell cell) {
        this.cell = cell;
        setPreferredSize(new Dimension(50, 50));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Aggiungi un MouseListener per gestire i clic sulla cella
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Se la cella è modificabile, aggiorna il valore (ad esempio, settiamo il valore 1)
                if (!cell.isShowed()) {
                    cell.setValueInsered(1);  // Impostiamo un valore di esempio per il clic
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cell.getValue().isPresent()) {
            g.setColor(Color.BLACK);
            g.drawString(cell.getValue().get().toString(), 20, 30);
        } else if (cell.isShowed()) {
            g.setColor(Color.RED);
            g.drawString("?", 20, 30);
        }
    }
}


