package controller;

import main.MainGUI;
import model.*;
import view.FenetreCatalogue;
import view.FenetrePanier;
import view.FenetreVente;
import view.StyleUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
package controller;

import view.InterfaceView;

public class InterfaceController {
    private InterfaceView view;

    public InterfaceController(InterfaceView view) {
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
