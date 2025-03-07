package pcd.ass03.part2.common.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackButtonFactory {

    public static JButton createBackButton(JFrame currentFrame, JFrame previousFrame) {
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
                    if (previousFrame != null) {
                        previousFrame.setVisible(true); // Mostra la finestra precedente
                    } else {
                        new HomeView(); // Se non c'è una finestra specificata, torna alla Home
                    }
                }
            }
        });

        return backButton;
    }
}
