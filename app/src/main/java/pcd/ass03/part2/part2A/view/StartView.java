package pcd.ass03.part2.part2A.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StartView extends JFrame {
    private final JButton openGridViewButton;
    private final JButton newGameButton;

    public StartView(String title) {

        openGridViewButton = new JButton("Join Game");
        openGridViewButton.setPreferredSize(new Dimension(150, 50));
        newGameButton = new JButton("New Game");
        newGameButton.setPreferredSize(new Dimension(150, 50));

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


        JLabel welcomeLabel = new JLabel("Bentornato "+title+"!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 1;
        add(welcomeLabel, gbc);

        add(openGridViewButton);
        add(newGameButton);
    }

    public void addJoinGameListener(ActionListener listener) {
        openGridViewButton.addActionListener(listener);
    }

    public void addNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }
}