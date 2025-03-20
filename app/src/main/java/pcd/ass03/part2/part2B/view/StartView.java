package pcd.ass03.part2.part2B.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartView extends JFrame {
    private final JButton joinButton;
    private final JButton createButton;

    public StartView(String title) {
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


        JLabel welcomeLabel = new JLabel("Benvenuto "+title+"!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridy = 1;
        add(welcomeLabel, gbc);

        createButton = new JButton("Nuovo");
        joinButton = new JButton("Unisciti");

        styleButton(createButton);
        styleButton(joinButton);

        Dimension buttonSize = new Dimension(150, 50);
        createButton.setPreferredSize(buttonSize);
        joinButton.setPreferredSize(buttonSize);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        add(createButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(joinButton, gbc);

        setVisible(true);

    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBackground(new Color(0x5C6BC0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 50));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(0x3F51B5));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(0x5C6BC0));
            }
        });
    }

    public void addJoinGameListener(ActionListener listener) {
        joinButton.addActionListener(listener);
    }

    public void addNewGameListener(ActionListener listener) {
        createButton.addActionListener(listener);
    }
}