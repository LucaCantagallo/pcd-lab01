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
    private String gamecode;

    public GameController(Rabbit user, GameView gameView, StartView startView, String gamecode) {
        this.user = user;
        this.gameView = gameView;
        this.startView = startView;
        this.gamecode = gamecode;
        user.addGridUpdateListener(this);
        gameView.displayGrid(user.getGridByGameCode(gamecode), user);
        gameView.addBackButtonListener(new BackButtonListener());
    }

    @Override
    public void onGridCreated() {

    }

    private boolean rightGrid(String gamecode){
        return this.gamecode.equals(gamecode);
    }

    @Override
    public void onGridUpdated(String gamecode) {
        if (gameView.isVisible() && rightGrid(gamecode)) {
            gameView.updateGrid(user.getGridByGameCode(gamecode));
        }
    }

    @Override
    public void onCellSelected(int gridId, int row, int col, Color color, String userId) {
        if (gameView.isVisible() && rightGrid(gamecode)) {
            gameView.colorCell(gridId, row, col, color);
        }
    }

    @Override
    public void onCellUnselected(int gridId, int row, int col) {
        if (gameView.isVisible() && rightGrid(gamecode)) {
            gameView.uncoloredCell(row, col);
        }
    }

    @Override
    public void onGridCompleted(int gridId, String userId) {
        if (gameView.isVisible() && rightGrid(gamecode)) {
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
