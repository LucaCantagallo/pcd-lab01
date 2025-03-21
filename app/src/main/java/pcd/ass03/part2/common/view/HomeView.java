package pcd.ass03.part2.common.view;

import pcd.ass03.part2.common.CommunicationProva.HandlerMessageDBGameCode;
import pcd.ass03.part2.common.CommunicationProva.HandlerSingleSudoku;
import pcd.ass03.part2.common.CommunicationProva.Rabbit;
import pcd.ass03.part2.common.sudoku.GameCodeDatabase;
import pcd.ass03.part2.common.sudoku.HomeAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class HomeView extends JFrame {

    private String nomeutente = "nomeutente"; //da impl
    private Rabbit rabbit;

    public HomeView(Rabbit rabbit)  {
        HandlerSingleSudoku.initialize(rabbit);
        HandlerMessageDBGameCode.initialize(rabbit);


        setTitle("Sudoku - Home");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("SUDOKU");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 48));
        titleLabel.setForeground(new Color(0xFF5733));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);


        JLabel welcomeLabel = new JLabel("Bentornato "+nomeutente+"!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 1;
        add(welcomeLabel, gbc);

        JButton createButton = new JButton("Crea");
        JButton playButton = new JButton("Gioca");
        styleButton(createButton);
        styleButton(playButton);

        Dimension buttonSize = new Dimension(150, 50);
        createButton.setPreferredSize(buttonSize);
        playButton.setPreferredSize(buttonSize);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gameCode;
                do {
                    gameCode = JOptionPane.showInputDialog(
                            HomeView.this, "Inserisci un gamecode per giocare con i tuoi amici!", "Inserisci Game Code", JOptionPane.PLAIN_MESSAGE);

                    // Aggiungi questo controllo per gestire il caso di annullamento
                    if (gameCode == null) {
                        return; // Esci dal metodo se l'utente ha annullato o chiuso la finestra
                    }

                    if (!gameCode.trim().isEmpty()) {
                        if (!HandlerMessageDBGameCode.isPresent(gameCode)) {
                            HandlerMessageDBGameCode.addGameCode(gameCode);
                            dispose();

                            new SudokuView(nomeutente, gameCode, HomeAction.CREATE, rabbit);
                            return;
                        } else {
                            JOptionPane.showMessageDialog(null, "Un sudoku con lo stesso nome presente! Cambia gamecode.", "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Non è stato inserito alcun gamecode! Inserire un gamecode.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } while(true);
            }
        });


        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gameCode;
                do {
                    gameCode = JOptionPane.showInputDialog(
                            HomeView.this, "Inserisci il gamecode per accedere al Sudoku!", "Inserisci Game Code", JOptionPane.PLAIN_MESSAGE);

                    // Aggiungi questo controllo per gestire il caso di annullamento
                    if (gameCode == null) {
                        return; // Esci dal metodo se l'utente ha annullato o chiuso la finestra
                    }

                    if (gameCode != null && !gameCode.trim().isEmpty()) {
                        if (HandlerMessageDBGameCode.isPresent(gameCode)) {
                            HomeView.this.dispose(); // Assicura di chiudere la finestra attuale
                            new SudokuView(nomeutente, gameCode, HomeAction.PLAY, rabbit);
                            return;
                        } else {
                            JOptionPane.showMessageDialog(null, "Il gamecode inserito non ha trovato nessuna corrispondenza! Riprova.", "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } while (true);
            }
        });



        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        add(createButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(playButton, gbc);

        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBackground(new Color(0x5C6BC0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 50));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0x3F51B5));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0x5C6BC0));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HomeView(new Rabbit());
        });
    }
}
