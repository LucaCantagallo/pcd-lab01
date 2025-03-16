package pcd.ass03.part2.part2A.controller;

import pcd.ass03.part2.part2A.model.Rabbit;
import pcd.ass03.part2.part2A.view.GameView;
import pcd.ass03.part2.part2A.view.StartView;

import java.awt.*;
import java.awt.event.ActionListener;

public class GameController implements GridUpdateListener{
    private final GameView gameView;
    private final StartView startView;
    private final Rabbit user;
    private final int selectedGrid;

    public GameController(Rabbit user, GameView gameView, StartView startView, int selectedGrid) {
        this.user = user;
        this.gameView = gameView;
        this.startView = startView;
        this.selectedGrid = selectedGrid;
        user.addGridUpdateListener(this);
        gameView.displayGrid(user.getGrid(selectedGrid), user);
        gameView.addBackButtonListener(new BackButtonListener());
    }

    @Override
    public void onGridCreated() {

    }

    @Override
    public void onGridUpdated(int gridIndex) {
        if (gameView.isVisible() && this.selectedGrid == (gridIndex - 1)) {
            gameView.updateGrid(user.getGrid(gridIndex - 1));
        }
    }

    @Override
    public void onCellSelected(int gridId, int row, int col, Color color, String userId) {
        if (gameView.isVisible() && this.selectedGrid == (gridId - 1)) {
            gameView.colorCell(gridId, row, col, color);
        }
    }

    @Override
    public void onCellUnselected(int gridId, int row, int col) {
        if (gameView.isVisible() && this.selectedGrid == (gridId - 1)) {
            gameView.uncoloredCell(row, col);
        }
    }

    @Override
    public void onGridCompleted(int gridId, String userId) {
        if (gameView.isVisible() && this.selectedGrid == gridId) {
            if (userId.equals(user.getId())) {
                return;
            }
            gameView.displayMessage("Congratulations! You have successfully completed the game.");
        }
    }

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            gameView.setVisible(false);
            startView.setVisible(true);
        }
    }
}
