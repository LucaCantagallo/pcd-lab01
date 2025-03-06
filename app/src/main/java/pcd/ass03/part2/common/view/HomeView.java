package pcd.ass03.part2.common.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeView extends JFrame {

    private String nomeutente = "nomeutente"; //da impl

    public HomeView() {
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
                String gameCode = JOptionPane.showInputDialog(
                        HomeView.this, "Inserisci un gamecode per giocare con i tuoi amici!", "Inserisci Game Code", JOptionPane.PLAIN_MESSAGE);

                if (gameCode != null && !gameCode.trim().isEmpty()) {
                    new SudokuView(gameCode);
                    dispose();
                }
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Play premuto: da implementare");
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
        SwingUtilities.invokeLater(HomeView::new);
    }
}
