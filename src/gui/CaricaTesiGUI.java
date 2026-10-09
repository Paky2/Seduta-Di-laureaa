package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import controller.Controller;
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

    private JButton caricaButton;
    private JButton chiudiButton;

    private JLabel messaggioLabel;

    private final Controller controller;
    private final String loginStudente;

    private Studente studente;
    private boolean richiestaAccettata;

    public CaricaTesiGUI(String loginStudente) {

        this.loginStudente = loginStudente;

        controller = new Controller();

        creaInterfaccia();
        inizializzaDati();
    }

    private void creaInterfaccia() {

        mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        JLabel titolo = new JLabel("Caricamento tesi");

        titolo.setFont(
                titolo.getFont().deriveFont(Font.BOLD, 17f)
        );

        messaggioLabel = new JLabel("Controllo requisiti...");

        JPanel intestazione = new JPanel(
                new GridLayout(2, 1, 5, 5)
        );

        intestazione.add(titolo);
        intestazione.add(messaggioLabel);

        fileField = new JTextField(24);
        versioneField = new JTextField("1", 8);
        sedutaComboBox = new JComboBox<>();

        sedutaComboBox.setRenderer(
                new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        JLabel label =
                                (JLabel) super.getListCellRendererComponent(
                                        list,
                                        value,
                                        index,
                                        isSelected,
                                        cellHasFocus
                                );

                        if (value instanceof SedutaLaurea seduta) {
                            label.setText(
                                    seduta.getData()
                                            + " - "
                                            + seduta.getOra()
                                            + " - "
                                            + seduta.getLuogo()
                            );
                        }

                        return label;
                    }
                }
        );

        JPanel form = new JPanel(
                new GridLayout(3, 2, 8, 8)
        );

        form.add(new JLabel("Nome o percorso del file:"));
        form.add(fileField);

        form.add(new JLabel("Versione:"));
        form.add(versioneField);

        form.add(new JLabel("Seduta di laurea:"));
        form.add(sedutaComboBox);

        JLabel notaRelatore = new JLabel(
                "Il relatore viene assegnato automaticamente "
                        + "in base alla richiesta accettata."
        );

        JPanel contenuto = new JPanel(
                new BorderLayout(5, 10)
        );

        contenuto.add(form, BorderLayout.CENTER);
        contenuto.add(notaRelatore, BorderLayout.SOUTH);

        caricaButton = new JButton("Carica tesi");
        chiudiButton = new JButton("Chiudi");

        JPanel pulsanti = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        pulsanti.add(caricaButton);
        pulsanti.add(chiudiButton);

        mainPanel.add(intestazione, BorderLayout.NORTH);
        mainPanel.add(contenuto, BorderLayout.CENTER);
        mainPanel.add(pulsanti, BorderLayout.SOUTH);

        caricaButton.addActionListener(
                e -> caricaTesi()
        );

        chiudiButton.addActionListener(
                e -> chiudiFinestra()
        );
    }

    private void inizializzaDati() {

        try {

            studente =
                    controller.OttieniStudente(loginStudente);

            if (studente == null) {

                impostaNonAbilitato(
                        "Studente non trovato."
                );

                return;
            }

            richiestaAccettata =
                    controller.StudenteHaRichiestaTirocinioAccettata(
                            studente.getMatricola()
                    );

            if (!richiestaAccettata) {

                impostaNonAbilitato(
                        "Non puoi caricare la tesi: "
                                + "devi avere una richiesta di tirocinio accettata."
                );

                return;
            }

            caricaSedute();

            if (sedutaComboBox.getItemCount() == 0) {

                impostaNonAbilitato(
                        "Non ci sono sedute disponibili."
                );

                return;
            }

            messaggioLabel.setText(
                    "Requisiti soddisfatti: puoi caricare la tesi."
            );

            messaggioLabel.setForeground(
                    new Color(0, 120, 0)
            );

            abilitaCampi(true);

        } catch (Exception e) {

            impostaNonAbilitato(
                    "Errore durante il controllo dei requisiti."
            );

            e.printStackTrace();
        }
    }

    private void caricaSedute() {

        List<SedutaLaurea> sedute =
                controller.ListaSedute();

        sedutaComboBox.removeAllItems();

        for (SedutaLaurea seduta : sedute) {

            if (seduta.isDisponibilita()) {
                sedutaComboBox.addItem(seduta);
            }
        }
    }

    private void caricaTesi() {

        try {

            studente =
                    controller.OttieniStudente(loginStudente);

            if (studente == null) {
                throw new IllegalStateException(
                        "Studente non trovato."
                );
            }

            /*
             * Secondo controllo lato applicazione.
             * Il DAO effettua comunque il controllo definitivo.
             */
            if (!controller.StudenteHaRichiestaTirocinioAccettata(
                    studente.getMatricola())) {

                impostaNonAbilitato(
                        "Non puoi caricare la tesi: "
                                + "non hai una richiesta accettata."
                );

                JOptionPane.showMessageDialog(
                        mainPanel,
                        messaggioLabel.getText(),
                        "Operazione non consentita",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String file =
                    fileField.getText().trim();

            String versioneTesto =
                    versioneField.getText().trim();

            if (file.isEmpty() || versioneTesto.isEmpty()) {

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

                versione = Integer.parseInt(versioneTesto);

                if (versione <= 0) {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "La versione deve essere un numero positivo.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            SedutaLaurea seduta =
                    (SedutaLaurea)
                            sedutaComboBox.getSelectedItem();

            if (seduta == null) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Seleziona una seduta disponibile.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            /*
             * Il relatore è null nel modello perché viene
             * ricavato dal DAO dalla richiesta accettata.
             */
            Tesi tesi = new Tesi(
                    0,
                    StatoTesi.IN_CORSO,
                    file,
                    versione,
                    studente,
                    null,
                    seduta
            );

            controller.CaricaTesi(tesi);

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Tesi caricata correttamente. "
                            ,
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            fileField.setText("");
            versioneField.setText("1");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Caricamento non effettuato:\n"
                            + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void impostaNonAbilitato(String messaggio) {

        messaggioLabel.setText(
                "<html><body style='width:300px'>"
                        + messaggio
                        + "</body></html>"
        );

        messaggioLabel.setForeground(Color.RED);

        abilitaCampi(false);
    }

    private void abilitaCampi(boolean abilitato) {

        fileField.setEnabled(abilitato);
        versioneField.setEnabled(abilitato);
        sedutaComboBox.setEnabled(abilitato);
        caricaButton.setEnabled(abilitato);
    }

    private void chiudiFinestra() {

        Window window =
                SwingUtilities.getWindowAncestor(mainPanel);

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
        panel1.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
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
        panel1.add(caricaButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chiudiButton = new JButton();
        chiudiButton.setText("Chiudi");
        panel1.add(chiudiButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sedutaComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        sedutaComboBox.setModel(defaultComboBoxModel1);
        panel1.add(sedutaComboBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }
}

