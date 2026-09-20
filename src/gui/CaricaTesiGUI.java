package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import controller.Controller;
import model.Docente;
import model.SedutaLaurea;
import model.StatoTesi;
import model.Studente;
import model.Tesi;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CaricaTesiGUI {

    private JPanel mainPanel;

    private JTextField fileField;
    private JTextField versioneField;

    private JComboBox<SedutaLaurea> sedutaComboBox;
    private JComboBox<Docente> relatoreComboBox;

    private JButton caricaButton;
    private JButton chiudiButton;

    private Controller controller;

    private String loginStudente;

    private List<SedutaLaurea> sedute;
    private List<Docente> docenti;

    public CaricaTesiGUI(String loginStudente) {

        this.loginStudente = loginStudente;

        controller = new Controller();

        // CREAZIONE ESPLICITA DELLA GUI
        mainPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        JLabel fileLabel =
                new JLabel("File:");

        fileField =
                new JTextField();

        JLabel versioneLabel =
                new JLabel("Versione:");

        versioneField =
                new JTextField();

        JLabel sedutaLabel =
                new JLabel("Seduta:");

        sedutaComboBox =
                new JComboBox<>();

        JLabel relatoreLabel =
                new JLabel("Relatore:");

        relatoreComboBox =
                new JComboBox<>();

        caricaButton =
                new JButton("Carica");

        chiudiButton =
                new JButton("Chiudi");

        mainPanel.add(fileLabel);
        mainPanel.add(fileField);

        mainPanel.add(versioneLabel);
        mainPanel.add(versioneField);

        mainPanel.add(sedutaLabel);
        mainPanel.add(sedutaComboBox);

        mainPanel.add(relatoreLabel);
        mainPanel.add(relatoreComboBox);

        // spazio vuoto
        mainPanel.add(new JLabel(""));
        mainPanel.add(caricaButton);

        mainPanel.add(new JLabel(""));
        mainPanel.add(chiudiButton);

        caricaSedute();
        caricaRelatori();

        caricaButton.addActionListener(
                e -> caricaTesi()
        );

        chiudiButton.addActionListener(
                e -> chiudiFinestra()
        );
    }

    private void caricaSedute() {

        sedute = controller.ListaSedute();

        sedutaComboBox.removeAllItems();

        for (SedutaLaurea seduta : sedute) {

            if (seduta.isDisponibilita()) {
                sedutaComboBox.addItem(seduta);
            }
        }
    }

    private void caricaRelatori() {

        docenti = controller.ListaDocenti();

        relatoreComboBox.removeAllItems();

        for (Docente docente : docenti) {
            relatoreComboBox.addItem(docente);
        }
    }

    private void caricaTesi() {

        String file =
                fileField.getText().trim();

        String versioneTesto =
                versioneField.getText().trim();

        if (file.isEmpty()
                || versioneTesto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Compila tutti i campi.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int versione;

        try {

            versione =
                    Integer.parseInt(versioneTesto);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "La versione deve essere un numero.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        SedutaLaurea seduta =
                (SedutaLaurea)
                        sedutaComboBox.getSelectedItem();

        Docente relatore =
                (Docente)
                        relatoreComboBox.getSelectedItem();

        if (seduta == null) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Seleziona una seduta disponibile.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (relatore == null) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Seleziona un relatore.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Studente studente =
                controller.OttieniStudente(
                        loginStudente
                );

        if (studente == null) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Studente non trovato.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Tesi tesi =
                new Tesi(
                        0,
                        StatoTesi.IN_CORSO,
                        file,
                        versione,
                        studente,
                        relatore,
                        seduta
                );

        try {

            controller.CaricaTesi(tesi);

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Tesi caricata correttamente.",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            fileField.setText("");
            versioneField.setText("");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore durante il caricamento della tesi:\n"
                            + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void chiudiFinestra() {

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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("File");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileField = new JTextField();
        panel1.add(fileField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Versione");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        versioneField = new JTextField();
        panel1.add(versioneField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Seduta");
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        caricaButton = new JButton();
        caricaButton.setText("Carica");
        panel1.add(caricaButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chiudiButton = new JButton();
        chiudiButton.setText("Chiudi");
        panel1.add(chiudiButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sedutaComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        sedutaComboBox.setModel(defaultComboBoxModel1);
        panel1.add(sedutaComboBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Relatore");
        panel1.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        relatoreComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        relatoreComboBox.setModel(defaultComboBoxModel2);
        panel1.add(relatoreComboBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }
}