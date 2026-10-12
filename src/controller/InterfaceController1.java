package controller;

import view.InterfaceView;

import javax.swing.*;

public class InterfaceController1 {
    private InterfaceView view;

    public InterfaceController1(InterfaceView view) {
        this.view = view;

        // On écoute le clic sur le bouton de la vue
        this.view.addBoutonActionListener(e -> traiterAction());
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceView view = new InterfaceView();
            new InterfaceController1(view);
            view.setVisible(true);
        });
    }
    private void traiterAction() {
        String texte = view.getTexteSaisi();

        // Logique métier (traitement des données)
        if (texte == null || texte.trim().isEmpty()) {
            view.afficherMessage("Erreur : Le champ est vide !");
        } else {
            // Traitement fictif ou appel à votre modèle/base de données
            String resultatTraite = "Succès ! Traitement de : " + texte;
            view.afficherMessage(resultatTraite);
        }
    }
}
