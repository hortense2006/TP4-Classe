package controller;

import view.InterfaceView;

public class InterfaceController1 {
    private InterfaceView view;

    public InterfaceController1(InterfaceView view) {
        this.view = view;

        // On écoute le clic sur le bouton de la vue
        this.view.addBoutonActionListener(e -> traiterAction());
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
