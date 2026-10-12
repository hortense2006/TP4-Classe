package Controller;

import Model.*;
import View.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationController {

    // ----- Modèle (TP4) -----
    private static final Client client = new Client();
    private static final Vendeur vendeur = new Vendeur();
    private static final BDD bdd = new BDD();
    private static final List<Article> catalogue = new ArrayList<>();
    private static final List<Article> panierAffiche = new ArrayList<>();
    private static String cheminPhoto = "photo.png";

    // ----- Fenêtres de la Q3 -----
    private static FenetreCatalogue fenCatalogue;
    private static FenetrePanier fenPanier;
    private static FenetreVente fenVente;

    private static final List<String> NAV = Arrays.asList("Catalogue", "Vendre", "Model.Panier", "Compte", "Model.Admin");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApplicationController::fenetreConnexion);
    }

    // ===== CONNEXION : listener + Model.AuthentificationException =====
    private static void fenetreConnexion() {
        JFrame f = new JFrame("Connexion");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(3, 2, 5, 5));
        JTextField champMail = new JTextField();
        JPasswordField champMdp = new JPasswordField();
        JButton btn = StyleUI.creerBouton("Se connecter");
        f.add(new JLabel(" Adresse e-mail :")); f.add(champMail);
        f.add(new JLabel(" Mot de passe :"));   f.add(champMdp);
        f.add(new JLabel());                    f.add(btn);

        btn.addActionListener(e -> {
            try {
                bdd.verifierIdentifiants(champMail.getText(), new String(champMdp.getPassword()));
                f.dispose();
                ouvrirApplication();
            } catch (AuthentificationException ex) {
                InterfaceView.erreur(f, ex.getMessage());
            }
        });
        f.setSize(420, 160);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private static void ouvrirApplication() {
        for (Article a : MainGUI.creerArticlesDemo()) {
            catalogue.add(a);
            vendeur.publierAnnonce(a);
        }
        fenCatalogue = new FenetreCatalogue(catalogue);
        fenPanier = new FenetrePanier(panierAffiche);
        fenVente = new FenetreVente();
        fenPanier.setLocation(680, 20);
        fenVente.setLocation(300, 140);
        fenCatalogue.setLocation(20, 20);

        brancherCatalogue();
        brancherPanier();
        brancherVente();
        fenCatalogue.setVisible(true);
    }

    // =====================================================================
    // BRANCHEMENT DES LISTENERS sur les fenêtres de la Q3
    // =====================================================================

    private static void brancherNavigation(JFrame f) {
        for (JButton b : InterfaceView.chercher(f, JButton.class)) {
            String cmd = b.getActionCommand();
            if (NAV.contains(cmd)) brancher(b, e -> naviguer(cmd));
        }
    }

    private static void brancherCatalogue() {
        brancherNavigation(fenCatalogue);

        List<JButton> ajouts = InterfaceView.boutons(fenCatalogue, "Ajouter au panier");
        for (int i = 0; i < ajouts.size() && i < catalogue.size(); i++) {
            Article a = catalogue.get(i);
            brancher(ajouts.get(i), e -> ajouterAuPanier(a));
        }

        JComboBox<String> choix = new JComboBox<>(
                catalogue.stream().map(Article::getTitre).toArray(String[]::new));
        JButton vendu = StyleUI.creerBouton("Marquer vendu");
        JButton lire = StyleUI.creerBouton("Lire articles.txt");
        vendu.addActionListener(e -> {
            int i = choix.getSelectedIndex();
            if (i >= 0) vendre(catalogue.get(i));
        });
        lire.addActionListener(e -> lireArticlesTxt());
        JPanel sud = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        sud.setBackground(StyleUI.FOND);
        sud.add(choix); sud.add(vendu); sud.add(lire);
        fenCatalogue.getContentPane().add(sud, BorderLayout.SOUTH);
    }

    private static void brancherPanier() {
        brancherNavigation(fenPanier);

        List<JButton> suppr = InterfaceView.boutons(fenPanier, "Suppr.");
        for (int i = 0; i < suppr.size() && i < panierAffiche.size(); i++) {
            Article a = panierAffiche.get(i);
            brancher(suppr.get(i), e -> retirerDuPanier(a));
        }

        JButton btnPayer = InterfaceView.boutons(fenPanier, "Payer").get(0);
        brancher(btnPayer, e -> payer());

        JButton achats = StyleUI.creerBouton("Lire achats.ser");
        achats.addActionListener(e -> lireAchatsSer());
        btnPayer.getParent().add(achats);
    }

    private static void brancherVente() {
        brancherNavigation(fenVente);
        JButton image = InterfaceView.boutons(fenVente, "Ajouter une image").get(0);
        brancher(image, e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(fenVente) == JFileChooser.APPROVE_OPTION) {
                cheminPhoto = fc.getSelectedFile().getPath();
                image.setText(fc.getSelectedFile().getName());
            }
        });
        brancher(InterfaceView.boutons(fenVente, "Publier").get(0), e -> publier());
    }

    @SuppressWarnings("rawtypes")
    private static void publier() {
        List<JTextField> champs = InterfaceView.chercher(fenVente, JTextField.class);
        List<JComboBox> combos = InterfaceView.chercher(fenVente, JComboBox.class);
        try {
            float prix = Float.parseFloat(champs.get(1).getText().trim().replace(',', '.'));
            if (prix <= 0) {
                throw new PrixInvalideException("Le prix doit être strictement supérieur à 0.");
            }
            String titre = champs.get(0).getText().trim();
            if (titre.isEmpty()) {
                InterfaceView.erreur(fenVente, "Le titre est obligatoire.");
                return;
            }
            String etat = (String) combos.get(1).getSelectedItem();
            String couleur = "Gris";
            for (JToggleButton t : InterfaceView.chercher(fenVente, JToggleButton.class)) {
                if (t.isSelected()) couleur = t.getActionCommand();
            }
            Article a = combos.get(0).getSelectedIndex() == 0
                    ? new Vetements(titre, prix, etat, couleur, cheminPhoto)
                    : new Accessoire(titre, prix, etat, couleur, cheminPhoto);

            vendeur.publierAnnonce(a);
            catalogue.add(a);
            champs.get(0).setText("");
            champs.get(1).setText("");
            naviguer("Catalogue");
        } catch (NumberFormatException ex) {
            InterfaceView.erreur(fenVente, "Le prix doit être un nombre.");
        } catch (PrixInvalideException ex) {
            InterfaceView.erreur(fenVente, ex.getMessage());
        }
    }

    // =====================================================================
    // ACTIONS
    // =====================================================================
    private static void naviguer(String page) {
        switch (page) {
            case "Catalogue": majCatalogue(); InterfaceView.montrer(fenCatalogue); break;
            case "Vendre":    InterfaceView.montrer(fenVente); break;
            case "Model.Panier":    majPanier(); InterfaceView.montrer(fenPanier); break;
            case "Compte":    client.modifierProfil(); InterfaceView.info(fenCatalogue, "Modifications du profil enregistrées."); break;
            default:          InterfaceView.info(fenCatalogue, "Page réservée aux administrateurs.");
        }
    }

    private static void ajouterAuPanier(Article a) {
        client.getPanier().ajouterArticle(a);
        panierAffiche.add(a);
        majPanier();
        InterfaceView.info(fenCatalogue, a.getTitre() + " ajouté au panier.");
    }

    private static void retirerDuPanier(Article a) {
        try {
            client.getPanier().retirerArticle(a);
            panierAffiche.remove(a);
            majPanier();
        } catch (ArticleNonTrouveException ex) {
            InterfaceView.erreur(fenPanier, ex.getMessage());
        }
    }

    private static void vendre(Article a) {
        try {
            vendeur.vendre(a);
            catalogue.remove(a);
            majCatalogue();
            InterfaceView.info(fenCatalogue, a.getTitre() + " vendu et enregistré dans articles.txt");
        } catch (ArticleNonTrouveException ex) {
            InterfaceView.erreur(fenCatalogue, ex.getMessage());
        }
    }

    private static void payer() {
        if (panierAffiche.isEmpty()) { InterfaceView.info(fenPanier, "Le panier est vide."); return; }
        for (Article a : new ArrayList<>(panierAffiche)) {
            client.acheter(a);
            try { client.getPanier().retirerArticle(a); } catch (ArticleNonTrouveException ignore) { }
        }
        panierAffiche.clear();
        majPanier();
        InterfaceView.info(fenPanier, "Commande validée. Achats sérialisés dans achats.ser");
    }

    private static void lireArticlesTxt() {
        List<Article> lus = GestionFichier.recupererArticles();
        StringBuilder sb = new StringBuilder();
        for (Article a : lus) sb.append(a.exporterFormatTexte()).append("\n");
        InterfaceView.afficherTexte(fenCatalogue, "Contenu de articles.txt", lus.isEmpty() ? "Fichier vide ou absent." : sb.toString());
    }

    private static void lireAchatsSer() {
        List<Article> lus = GestionSerialization.charger("achats.ser");
        StringBuilder sb = new StringBuilder();
        for (Article a : lus) {
            sb.append(a.genererReference()).append(" | ").append(a.getTitre())
                    .append(" | ").append(a.getPrix()).append(" €\n");
        }
        InterfaceView.afficherTexte(fenPanier, "Achats désérialisés", lus.isEmpty() ? "Aucun achat." : sb.toString());
    }

    // =====================================================================
    // Rafraîchissement
    // =====================================================================
    private static void majCatalogue() {
        fenCatalogue.setContentPane(fenCatalogue.creerContenu());
        brancherCatalogue();
        fenCatalogue.validate();
        fenCatalogue.repaint();
    }

    private static void majPanier() {
        fenPanier.setContentPane(fenPanier.creerContenu());
        brancherPanier();
        fenPanier.validate();
        fenPanier.repaint();
    }

    // =====================================================================
    // Utilitaires du Contrôleur
    // =====================================================================
    private static void brancher(JButton b, ActionListener l) {
        for (ActionListener old : b.getActionListeners()) b.removeActionListener(old);
        b.addActionListener(l);
    }
}
