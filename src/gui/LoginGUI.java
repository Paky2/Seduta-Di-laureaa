package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import controller.Controller;
import model.Docente;

import javax.swing.*;
import java.awt.*;

public class LoginGUI {

    private JPanel mainPanel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registrazioneButton;
    private JComboBox<String> tipoUtenteComboBox;

    private Controller controller;

    public LoginGUI() {

        controller = new Controller();

        loginButton.addActionListener(
                e -> login()
        );

        registrazioneButton.addActionListener(
                e -> apriRegistrazione()
        );
    }

    private void login() {

        String login =
                loginField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        String tipo =
                (String)
                        tipoUtenteComboBox.getSelectedItem();

        if (login.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Inserisci login e password.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if ("Studente".equals(tipo)) {

            if (!controller.LoginStudente(
                    login,
                    password)) {

                mostraErrore();
                return;
            }

            apriHomeStudente(login);
            return;
        }

        if ("Docente".equals(tipo)) {

            if (!controller.LoginDocente(
                    login,
                    password)) {

                mostraErrore();
                return;
            }

            Docente docente =
                    controller.OttieniDocente(login);

            apriHomeDocente(docente);
            return;
        }

        if ("Coordinatore".equals(tipo)) {

            if (!controller.LoginCoordinatore(
                    login,
                    password)) {

                mostraErrore();
                return;
            }

            Docente coordinatore =
                    controller.OttieniCoordinatore(
                            login
                    );

            apriHomeCoordinatore(coordinatore);
        }
    }

    private void apriHomeStudente(
            String login) {

        HomeStudenteGUI gui =
                new HomeStudenteGUI(login);

        JFrame frame =
                new JFrame("Home Studente");

        frame.setContentPane(
                gui.getMainPanel()
        );

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        chiudiLogin();
    }

    private void apriHomeDocente(
            Docente docente) {

        HomeDocenteGUI gui =
                new HomeDocenteGUI(docente);

        JFrame frame =
                new JFrame("Home Docente");

        frame.setContentPane(
                gui.getMainPanel()
        );

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        chiudiLogin();
    }

    private void apriHomeCoordinatore(
            Docente coordinatore) {

        HomeCoordinatoreGUI gui =
                new HomeCoordinatoreGUI(
                        coordinatore
                );

        JFrame frame =
                new JFrame("Home Coordinatore");

        frame.setContentPane(
                gui.getMainPanel()
        );

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        chiudiLogin();
    }

    private void apriRegistrazione() {

        String tipo =
                (String) tipoUtenteComboBox.getSelectedItem();

        if ("Studente".equals(tipo)) {

            RegistrazioneStudenteGUI gui =
                    new RegistrazioneStudenteGUI();

            JFrame frame =
                    new JFrame("Registrazione Studente");

            frame.setContentPane(gui.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } else if ("Docente".equals(tipo)) {

            RegistrazioneDocenteGUI gui =
                    new RegistrazioneDocenteGUI();

            JFrame frame =
                    new JFrame("Registrazione Docente");

            frame.setContentPane(gui.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } else if ("Coordinatore".equals(tipo)) {

            RegistrazioneDocenteGUI gui =
                    new RegistrazioneDocenteGUI(true);

            JFrame frame =
                    new JFrame("Registrazione Coordinatore");

            frame.setContentPane(gui.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    private void mostraErrore() {

        JOptionPane.showMessageDialog(
                mainPanel,
                "Login o password non corretti.",
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void chiudiLogin() {

        Window window =
                SwingUtilities.getWindowAncestor(
                        mainPanel
                );

        if (window != null) {
            window.dispose();
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(6, 5, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(1, 4, 5, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Login");
        mainPanel.add(label1, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Login");
        mainPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Password");
        mainPanel.add(label3, new GridConstraints(2, 0, 4, 2, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loginField = new JTextField();
        loginField.setText("");
        mainPanel.add(loginField, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passwordField = new JPasswordField();
        mainPanel.add(passwordField, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        loginButton = new JButton();
        loginButton.setText("Accedi");
        mainPanel.add(loginButton, new GridConstraints(4, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registrazioneButton = new JButton();
        registrazioneButton.setText("Registrati");
        mainPanel.add(registrazioneButton, new GridConstraints(5, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tipoUtenteComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Studente");
        defaultComboBoxModel1.addElement("Docente");
        defaultComboBoxModel1.addElement("Coordinatore");
        tipoUtenteComboBox.setModel(defaultComboBoxModel1);
        mainPanel.add(tipoUtenteComboBox, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Tipo Utente");
        mainPanel.add(label4, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}