import Database.DatabaseInitializer;
import gui.LoginGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {

            DatabaseInitializer.initialize();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Impossibile inizializzare il database.\n\n"
                            + e.getMessage(),
                    "Errore database",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();

            return;
        }

        SwingUtilities.invokeLater(() -> {

            LoginGUI loginGUI = new LoginGUI();

            JFrame frame =
                    new JFrame("Seduta Di Laurea");

            frame.setContentPane(
                    loginGUI.getMainPanel()
            );

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}