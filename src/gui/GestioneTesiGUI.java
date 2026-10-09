package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import controller.Controller;
import model.Docente;
import model.StatoTesi;
import model.Tesi;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GestioneTesiGUI {

    private JPanel mainPanel;
    private JList<String> tesiList;

    private JButton accettaButton;
    private JButton rifiutaButton;
    private JButton chiudiButton;

    private JLabel titoloLabel;

    private final Controller controller;
    private final Docente docente;

    private List<Tesi> tesi =
            new ArrayList<>();

    /*
     * Costruttore per il coordinatore:
     * consultazione, senza pulsanti di valutazione.
     */
    public GestioneTesiGUI() {
        this(null);
    }

    /*
     * Costruttore per il docente autenticato.
     */
    public GestioneTesiGUI(Docente docente) {

        this.docente = docente;

        controller = new Controller();

        creaInterfaccia();

        accettaButton.addActionListener(
                e -> aggiornaTesi(true)
        );

        rifiutaButton.addActionListener(
                e -> aggiornaTesi(false)
        );

        chiudiButton.addActionListener(
                e -> chiudiFinestra()
        );

        if (docente == null) {

            titoloLabel.setText(
                    "Consultazione delle tesi già valutate"
            );

            accettaButton.setVisible(false);
            rifiutaButton.setVisible(false);

        } else {

            titoloLabel.setText(
                    "Tesi in attesa assegnate al docente "
                            + docente.getIdDocente()
            );
        }

        caricaTesi();
    }

    private void creaInterfaccia() {

        mainPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        titoloLabel = new JLabel("Gestione tesi");

        titoloLabel.setFont(
                titoloLabel.getFont().deriveFont(
                        Font.BOLD,
                        16f
                )
        );

        tesiList = new JList<>();

        tesiList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(tesiList);

        scrollPane.setPreferredSize(
                new Dimension(650, 260)
        );

        accettaButton = new JButton("Accetta");
        rifiutaButton = new JButton("Rifiuta");
        chiudiButton = new JButton("Chiudi");

        JPanel pulsanti = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        pulsanti.add(accettaButton);
        pulsanti.add(rifiutaButton);
        pulsanti.add(chiudiButton);

        mainPanel.add(
                titoloLabel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                pulsanti,
                BorderLayout.SOUTH
        );
    }

    private void caricaTesi() {

        try {

            if (docente != null) {

                /*
                 * Il docente riceve soltanto le proprie
                 * tesi IN_CORSO.
                 */
                tesi = controller.ListaTesiPerRelatore(
                        docente.getIdDocente()
                );

            } else {

                /*
                 * Il coordinatore vede solo le tesi già
                 * accettate o rifiutate.
                 */
                tesi = controller.ListaTesiValutate();
            }

            DefaultListModel<String> model =
                    new DefaultListModel<>();

            if (tesi.isEmpty()) {

                model.addElement(
                        docente == null
                                ? "Nessuna tesi già valutata."
                                : "Non ci sono tesi in attesa assegnate a te."
                );

            } else {

                for (Tesi t : tesi) {

                    model.addElement(
                            "ID: " + t.getId()
                                    + " | Matricola: "
                                    + t.getStudente().getMatricola()
                                    + " | File: "
                                    + t.getFile()
                                    + " | Versione: "
                                    + t.getVersione()
                                    + " | Stato: "
                                    + t.getStato()
                    );
                }
            }

            tesiList.setModel(model);

        } catch (Exception e) {

            tesi = new ArrayList<>();

            DefaultListModel<String> model =
                    new DefaultListModel<>();

            model.addElement(
                    "Errore durante il caricamento delle tesi."
            );

            tesiList.setModel(model);

            JOptionPane.showMessageDialog(
                    mainPanel,
                    e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void aggiornaTesi(boolean accetta) {

        /*
         * Questa schermata non permette al coordinatore
         * di accettare o rifiutare tesi.
         */
        if (docente == null) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Solo il docente tutor può valutare una tesi.",
                    "Operazione non consentita",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int indice =
                tesiList.getSelectedIndex();

        if (indice < 0 || indice >= tesi.size()) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Seleziona una tesi.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Tesi selezionata =
                tesi.get(indice);

        StatoTesi statoPrecedente =
                selezionata.getStato();

        selezionata.setStato(
                accetta
                        ? StatoTesi.ACCETTATA
                        : StatoTesi.RIFIUTATA
        );

        try {

            controller.AccettaORifiutaTesi(
                    selezionata,
                    docente.getIdDocente()
            );

            JOptionPane.showMessageDialog(
                    mainPanel,
                    accetta
                            ? "Tesi accettata."
                            : "Tesi rifiutata.",
                    "Operazione completata",
                    JOptionPane.INFORMATION_MESSAGE
            );

            caricaTesi();

        } catch (Exception e) {

            selezionata.setStato(statoPrecedente);

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Non è stato possibile valutare la tesi:\n"
                            + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();

            caricaTesi();
        }
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        accettaButton = new JButton();
        accettaButton.setText("Accetta");
        mainPanel.add(accettaButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rifiutaButton = new JButton();
        rifiutaButton.setText("Rifiuta");
        mainPanel.add(rifiutaButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chiudiButton = new JButton();
        chiudiButton.setText("Chiudi");
        mainPanel.add(chiudiButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tesiList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        tesiList.setModel(defaultListModel1);
        scrollPane1.setViewportView(tesiList);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}