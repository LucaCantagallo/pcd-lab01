package pcd.ass03.part2.part2A.controller;


import pcd.ass03.part2.part2A.model.Rabbit;
import pcd.ass03.part2.part2A.view.GameView;
import pcd.ass03.part2.part2A.view.StartView;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class StartController {

    private final StartView startView;
    private final Rabbit user;
    private GameView detailsView;

    public StartController(StartView startView, Rabbit user) {
        this.startView = startView;
        this.user = user;
        this.startView.addJoinGameListener(new OpenGridViewListener());
        this.startView.addNewGameListener(new NewGameListener());
    }

    class OpenGridViewListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            String gameCode;
            do {
                gameCode = JOptionPane.showInputDialog(
                        startView, "Inserisci il gamecode per accedere al Sudoku!", "Inserisci Game Code", JOptionPane.PLAIN_MESSAGE);

                // Aggiungi questo controllo per gestire il caso di annullamento
                if (gameCode == null) {
                    return; // Esci dal metodo se l'utente ha annullato o chiuso la finestra
                }

                if (!gameCode.trim().isEmpty()) {
                    if (user.isPresent(gameCode)) {
                        startView.setVisible(false);
                        //CAMBIA DA QUI
                        detailsView = new GameView(user.getId(), gameCode);
                        new GameController(user, detailsView, startView, gameCode);
                        detailsView.setVisible(true);
                        startView.setVisible(false);
                        //GridListView gridListView = new GridListView(user.getId());
                        //new GridListController(user, startView, gridListView);
                        //A QUI
                        return;
                    } else {
                        JOptionPane.showMessageDialog(null, "Il gamecode inserito non ha trovato nessuna corrispondenza! Riprova.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } while (true);

        }
    }

    class NewGameListener implements ActionListener {


        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            String gameCode;
            do {
                gameCode = JOptionPane.showInputDialog(
                        startView, "Inserisci un gamecode per giocare con i tuoi amici!", "Inserisci Game Code", JOptionPane.PLAIN_MESSAGE);

                // Aggiungi questo controllo per gestire il caso di annullamento
                if (gameCode == null) {
                    return; // Esci dal metodo se l'utente ha annullato o chiuso la finestra
                }

                if (!gameCode.trim().isEmpty()) {
                    if (!user.isPresent(gameCode)) {
                        try {
                            user.createGrid(gameCode);
                            //TODO: collegare all view
                        } catch (IOException | TimeoutException ex) {
                            throw new RuntimeException(ex);
                        }
                        GameView gameView = new GameView(user.getId(), gameCode);
                        new GameController(user, gameView, startView, gameCode);
                        startView.setVisible(false);
                        gameView.setVisible(true);
                        System.out.println(user.getGameCodeList());
                        return;
                    } else {
                        JOptionPane.showMessageDialog(null, "Un sudoku con lo stesso nome presente! Cambia gamecode.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Non è stato inserito alcun gamecode! Inserire un gamecode.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } while(true);


        }
    }


}
