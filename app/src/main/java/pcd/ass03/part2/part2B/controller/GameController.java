package pcd.ass03.part2.part2B.controller;

import pcd.ass03.part2.part2B.model.RMI;
import pcd.ass03.part2.part2B.view.GameView;
import pcd.ass03.part2.part2B.view.StartView;

import java.awt.*;
import java.awt.event.ActionListener;

public class GameController implements GridUpdateListener{

    private final GameView gameView;
    private final StartView startView;
    private final RMI user;
    private String gamecode;

    public GameController(RMI user, GameView gameview, StartView startview, String gamecode) {
        this.gameView = gameview;
        this.startView = startview;
        this.user = user;
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
    public void onCellSelected(String gamecode, int row, int col, Color color) {
        if (gameView.isVisible() && rightGrid(gamecode)) {
            gameView.colorCell(gamecode, row, col, color);
        }
    }

    @Override
    public void onCellUnselected(String gamecode, int row, int col) {
        if (gameView.isVisible() && rightGrid(gamecode)) {
            gameView.uncoloredCell(row, col);
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
