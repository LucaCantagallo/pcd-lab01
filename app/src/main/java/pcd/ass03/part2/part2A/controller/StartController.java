package pcd.ass03.part2.part2A.controller;

import pcd.ass03.part2.part2A.model.Rabbit;
import pcd.ass03.part2.part2A.view.GameView;
import pcd.ass03.part2.part2A.view.GridListView;
import pcd.ass03.part2.part2A.view.StartView;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class StartController {

    private final StartView startView;
    private final Rabbit user;

    public StartController(StartView startView, Rabbit user) {
        this.startView = startView;
        this.user = user;
        this.startView.addJoinGameListener(new OpenGridViewListener());
        this.startView.addNewGameListener(new NewGameListener());
    }

    class OpenGridViewListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            startView.setVisible(false);
            GridListView gridListView = new GridListView(user.getId());
            new GridListController(user, startView, gridListView);
            gridListView.setVisible(true);
        }
    }

    class NewGameListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            try {
                String gameCode = "prova";
                user.createGrid(gameCode);
                //TODO: collegare all view
            } catch (IOException | TimeoutException ex) {
                throw new RuntimeException(ex);
            }
            GameView gameView = new GameView(user.getId());
            new GameController(user, gameView, startView, user.getAllGrids().size() - 1);
            startView.setVisible(false);
            gameView.setVisible(true);
        }
    }


}
