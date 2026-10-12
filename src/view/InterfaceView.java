package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InterfaceView extends JFrame {
    private JButton boutonAction;
    private JTextField champSaisie;
    private JLabel labelResultat;

    public InterfaceView() {
        setTitle("TP4 - Gestion");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialisation des composants graphiques
        setLayout(new FlowLayout());
        champSaisie = new JTextField(15);
        boutonAction = new JButton("Valider");
        labelResultat = new JLabel("En attente d'une action...");

        // Ajout des composants à la fenêtre
        add(new JLabel("Entrez une valeur :"));
        add(champSaisie);
        add(boutonAction);
        add(labelResultat);
    }

    // Méthodes pour permettre au contrôleur d'interagir avec la vue
    public String getTexteSaisi() {
        return champSaisie.getText();
    }

    public void afficherMessage(String message) {
        labelResultat.setText(message);
    }

    // Permet d'attacher un écouteur d'événements venant du contrôleur
    public void addBoutonActionListener(ActionListener listener) {
        boutonAction.addActionListener(listener);
    }
}
