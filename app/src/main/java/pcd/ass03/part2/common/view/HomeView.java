package pcd.ass03.part2.common.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeView extends JFrame {

    public HomeView() {
        // Impostazioni della finestra principale
        setTitle("Sudoku - Home");
        setSize(600, 600); // Impostiamo la dimensione della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); // Layout per un posizionamento preciso dei componenti

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Aggiungi margini (spazi) tra i componenti

        // Creiamo una JLabel per il titolo "SUDOKU"
        JLabel titleLabel = new JLabel("SUDOKU");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 48)); // Font più grande e strano
        titleLabel.setForeground(new Color(0xFF5733)); // Colore arancione per il testo

        // Posizioniamo il titolo al centro della finestra (colonna 0, riga 0)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Il titolo occupa una sola colonna
        gbc.gridheight = 1; // Una sola riga
        gbc.anchor = GridBagConstraints.CENTER; // Centra il componente
        add(titleLabel, gbc);

        // Creiamo un pulsante "Play"
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.PLAIN, 20)); // Impostiamo il font per il pulsante
        playButton.setBackground(new Color(0x5C6BC0)); // Colore blu per il pulsante
        playButton.setForeground(Color.WHITE); // Colore del testo del pulsante
        playButton.setFocusPainted(false); // Rimuove il bordo al focus

        // Aggiungiamo un effetto hover sul pulsante
        playButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playButton.setBackground(new Color(0x3F51B5)); // Cambia il colore quando il mouse passa sopra
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                playButton.setBackground(new Color(0x5C6BC0)); // Ritorna al colore originale
            }
        });

        // Azione del pulsante: aprire la vista del Sudoku
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Quando il pulsante viene premuto, apriamo la vista del Sudoku
                new SudokuView();
                // Chiudiamo la schermata Home
                dispose();
            }
        });

        // Posizioniamo il pulsante sotto il titolo (colonna 0, riga 1)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Il pulsante occupa una sola colonna
        gbc.gridheight = 1; // Una sola riga
        gbc.anchor = GridBagConstraints.CENTER; // Centra il pulsante
        add(playButton, gbc);

        // Rende la finestra visibile
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomeView::new);
    }
}
