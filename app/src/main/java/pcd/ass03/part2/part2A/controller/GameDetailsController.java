package pcd.ass03.part2.part2A.controller;

import pcd.ass03.part2.part2A.model.Rabbit;
import pcd.ass03.part2.part2A.view.GameDetailsView;
import pcd.ass03.part2.part2A.view.StartView;

import java.awt.*;
import java.awt.event.ActionListener;

public class GameDetailsController implements GridUpdateListener{
    private final GameDetailsView gameDetailsView;
    private final StartView startView;
    private final Rabbit user;
    private final int selectedGrid;

    public GameDetailsController(Rabbit user, GameDetailsView gameDetailsView, StartView startView, int selectedGrid) {
        this.user = user;
        this.gameDetailsView = gameDetailsView;
        this.startView = startView;
        this.selectedGrid = selectedGrid;
        user.addGridUpdateListener(this);
        gameDetailsView.displayGrid(user.getGrid(selectedGrid), user);
        gameDetailsView.addBackButtonListener(new BackButtonListener());
    }

    @Override
    public void onGridCreated() {

    }

    @Override
    public void onGridUpdated(int gridIndex) {
        if (gameDetailsView.isVisible() && this.selectedGrid == (gridIndex - 1)) {
            gameDetailsView.updateGrid(user.getGrid(gridIndex - 1));
        }
    }

    @Override
    public void onCellSelected(int gridId, int row, int col, Color color, String userId) {
        if (gameDetailsView.isVisible() && this.selectedGrid == (gridId - 1)) {
            gameDetailsView.colorCell(gridId, row, col, color);
        }
    }

    @Override
    public void onCellUnselected(int gridId, int row, int col) {
        if (gameDetailsView.isVisible() && this.selectedGrid == (gridId - 1)) {
            gameDetailsView.uncoloredCell(row, col);
        }
    }

    @Override
    public void onGridCompleted(int gridId, String userId) {
        if (gameDetailsView.isVisible() && this.selectedGrid == gridId) {
            if (userId.equals(user.getId())) {
                return;
            }
            gameDetailsView.displayMessage("Congratulations! You have successfully completed the game.");
        }
    }

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            gameDetailsView.setVisible(false);
            startView.setVisible(true);
        }
    }
}
