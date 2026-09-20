import gui.LoginGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LoginGUI loginGUI = new LoginGUI();

            JFrame frame = new JFrame("Seduta di Laurea");

            frame.setContentPane(loginGUI.getMainPanel());

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setSize(300, 250);

            frame.setLocationRelativeTo(null);

            frame.setVisible(true);
        });
    }
}