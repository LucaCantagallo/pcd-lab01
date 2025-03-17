package pcd.ass03.part2.common.view;

import pcd.ass03.part2.common.CommunicationProva.Rabbit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackButtonFactory {

    public static JButton createBackButton(Rabbit rabbit, JFrame currentFrame) {
        JButton backButton = new JButton("Indietro");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        currentFrame,
                        "Sei sicuro di voler tornare alla pagina precedente?",
                        "Conferma",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (response == JOptionPane.YES_OPTION) {
                    currentFrame.dispose(); // Chiude la finestra attuale

                    SwingUtilities.invokeLater(() -> new HomeView(rabbit)); // La creazione di HomeView avviene nel thread giusto

                }
            }
        });

        return backButton;
    }
}
