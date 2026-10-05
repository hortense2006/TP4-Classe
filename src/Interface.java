import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Question 4 : contrôleur de l'interface.
 * Les fenêtres de la Q3 (FenetreCatalogue, FenetrePanier, FenetreVente, StyleUI) restent INCHANGÉES :
 * on les instancie, puis on retrouve leurs boutons dans l'arbre de composants Swing
 * pour y brancher les LISTENERS (exceptions, fichier texte, sérialisation).
 */
public class Interface {

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

    private static final List<String> NAV = Arrays.asList("Catalogue", "Vendre", "Panier", "Compte", "Admin");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Interface::fenetreConnexion);
    }

    // ===== CONNEXION : listener + AuthentificationException =====
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

        btn.addActionListener(e -> {                      // LISTENER : clic sur "Se connecter"
            try {
                bdd.verifierIdentifiants(champMail.getText(), new String(champMdp.getPassword()));
                f.dispose();
                ouvrirApplication();
            } catch (AuthentificationException ex) {      // identifiants faux
                erreur(f, ex.getMessage());
            }
        });
        f.setSize(420, 160);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private static void ouvrirApplication() {
        for (Article a : MainGUI.creerArticlesDemo()) {
            catalogue.add(a);
            vendeur.publierAnnonce(a);                    // nécessaire pour pouvoir "vendre"
        }
        fenCatalogue = new FenetreCatalogue(catalogue);   // constructeurs de la Q3, non modifiés
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

    /** Barre de navigation : les boutons ont pour actionCommand "Catalogue", "Vendre", "Panier"... */
    private static void brancherNavigation(JFrame f) {
        for (JButton b : chercher(f, JButton.class)) {
            String cmd = b.getActionCommand();
            if (NAV.contains(cmd)) brancher(b, e -> naviguer(cmd));
        }
    }

    private static void brancherCatalogue() {
        brancherNavigation(fenCatalogue);

        // Les cartes sont dans l'ordre de la liste : le i-ème bouton correspond au i-ème article
        List<JButton> ajouts = boutons(fenCatalogue, "Ajouter au panier");
        for (int i = 0; i < ajouts.size() && i < catalogue.size(); i++) {
            Article a = catalogue.get(i);
            brancher(ajouts.get(i), e -> ajouterAuPanier(a));      // LISTENER : ajout au panier
        }

        // Barre d'actions ajoutée en bas de la fenêtre (zone SOUTH libre)
        JComboBox<String> choix = new JComboBox<>(
                catalogue.stream().map(Article::getTitre).toArray(String[]::new));
        JButton vendu = StyleUI.creerBouton("Marquer vendu");
        JButton lire = StyleUI.creerBouton("Lire articles.txt");
        vendu.addActionListener(e -> {                              // LISTENER : vente -> articles.txt
            int i = choix.getSelectedIndex();
            if (i >= 0) vendre(catalogue.get(i));
        });
        lire.addActionListener(e -> lireArticlesTxt());             // LISTENER : lecture fichier texte
        JPanel sud = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        sud.setBackground(StyleUI.FOND);
        sud.add(choix); sud.add(vendu); sud.add(lire);
        fenCatalogue.getContentPane().add(sud, BorderLayout.SOUTH);
    }

    private static void brancherPanier() {
        brancherNavigation(fenPanier);

        List<JButton> suppr = boutons(fenPanier, "Suppr.");
        for (int i = 0; i < suppr.size() && i < panierAffiche.size(); i++) {
            Article a = panierAffiche.get(i);
            brancher(suppr.get(i), e -> retirerDuPanier(a));        // LISTENER : retrait du panier
        }

        JButton btnPayer = boutons(fenPanier, "Payer").get(0);
        brancher(btnPayer, e -> payer());                           // LISTENER : paiement -> sérialisation

        JButton achats = StyleUI.creerBouton("Lire achats.ser");
        achats.addActionListener(e -> lireAchatsSer());             // LISTENER : désérialisation
        btnPayer.getParent().add(achats);                           // à côté du bouton Payer
    }

    private static void brancherVente() {
        brancherNavigation(fenVente);
        JButton image = boutons(fenVente, "Ajouter une image").get(0);
        brancher(image, e -> {                                      // LISTENER : choix d'une image
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(fenVente) == JFileChooser.APPROVE_OPTION) {
                cheminPhoto = fc.getSelectedFile().getPath();
                image.setText(fc.getSelectedFile().getName());
            }
        });
        brancher(boutons(fenVente, "Publier").get(0), e -> publier()); // LISTENER : publication
    }

    /** Relit le formulaire de FenetreVente, contrôle les saisies (exceptions) et publie l'article. */
    @SuppressWarnings("rawtypes")
    private static void publier() {
        List<JTextField> champs = chercher(fenVente, JTextField.class);   // 1er = titre, 2e = prix
        List<JComboBox> combos = chercher(fenVente, JComboBox.class);     // 1er = catégorie, 2e = état
        try {
            float prix = Float.parseFloat(champs.get(1).getText().trim().replace(',', '.')); // NumberFormatException
            if (prix <= 0) {
                throw new PrixInvalideException("Le prix doit être strictement supérieur à 0.");
            }
            String titre = champs.get(0).getText().trim();
            if (titre.isEmpty()) {
                erreur(fenVente, "Le titre est obligatoire.");
                return;
            }
            String etat = (String) combos.get(1).getSelectedItem();
            String couleur = "Gris";
            for (JToggleButton t : chercher(fenVente, JToggleButton.class)) {
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
            erreur(fenVente, "Le prix doit être un nombre.");
        } catch (PrixInvalideException ex) {
            erreur(fenVente, ex.getMessage());
        }
    }

    // =====================================================================
    // ACTIONS
    // =====================================================================
    private static void naviguer(String page) {
        switch (page) {
            case "Catalogue": majCatalogue(); montrer(fenCatalogue); break;
            case "Vendre":    montrer(fenVente); break;
            case "Panier":    majPanier(); montrer(fenPanier); break;
            case "Compte":    client.modifierProfil(); info(fenCatalogue, "Modifications du profil enregistrées."); break;
            default:          info(fenCatalogue, "Page réservée aux administrateurs.");
        }
    }

    private static void ajouterAuPanier(Article a) {
        client.getPanier().ajouterArticle(a);
        panierAffiche.add(a);
        majPanier();
        info(fenCatalogue, a.getTitre() + " ajouté au panier.");
    }

    private static void retirerDuPanier(Article a) {
        try {
            client.getPanier().retirerArticle(a);         // ArticleNonTrouveException
            panierAffiche.remove(a);
            majPanier();
        } catch (ArticleNonTrouveException ex) {
            erreur(fenPanier, ex.getMessage());
        }
    }

    private static void vendre(Article a) {
        try {
            vendeur.vendre(a);                            // écrit puis relit articles.txt
            catalogue.remove(a);
            majCatalogue();
            info(fenCatalogue, a.getTitre() + " vendu et enregistré dans articles.txt");
        } catch (ArticleNonTrouveException ex) {
            erreur(fenCatalogue, ex.getMessage());
        }
    }

    private static void payer() {
        if (panierAffiche.isEmpty()) { info(fenPanier, "Le panier est vide."); return; }
        for (Article a : new ArrayList<>(panierAffiche)) {
            client.acheter(a);                            // sérialise dans achats.ser
            try { client.getPanier().retirerArticle(a); } catch (ArticleNonTrouveException ignore) { }
        }
        panierAffiche.clear();
        majPanier();
        info(fenPanier, "Commande validée. Achats sérialisés dans achats.ser");
    }

    private static void lireArticlesTxt() {
        List<Article> lus = GestionFichier.recupererArticles();
        StringBuilder sb = new StringBuilder();
        for (Article a : lus) sb.append(a.exporterFormatTexte()).append("\n");
        afficherTexte(fenCatalogue, "Contenu de articles.txt", lus.isEmpty() ? "Fichier vide ou absent." : sb.toString());
    }

    private static void lireAchatsSer() {
        List<Article> lus = GestionSerialization.charger("achats.ser");
        StringBuilder sb = new StringBuilder();
        for (Article a : lus) {
            sb.append(a.genererReference()).append(" | ").append(a.getTitre())
                    .append(" | ").append(a.getPrix()).append(" €\n");
        }
        afficherTexte(fenPanier, "Achats désérialisés", lus.isEmpty() ? "Aucun achat." : sb.toString());
    }

    // =====================================================================
    // Rafraîchissement : on reconstruit la page (creerContenu() de la Q3) puis on rebranche
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
    // Utilitaires
    // =====================================================================
    /** Retire les anciens listeners (le bouton Payer est réutilisé à chaque reconstruction) puis en ajoute un. */
    private static void brancher(JButton b, ActionListener l) {
        for (ActionListener old : b.getActionListeners()) b.removeActionListener(old);
        b.addActionListener(l);
    }

    /** Parcourt récursivement l'arbre de composants et renvoie ceux du type demandé, dans l'ordre. */
    private static <T extends Component> List<T> chercher(Container c, Class<T> type) {
        List<T> res = new ArrayList<>();
        for (Component comp : c.getComponents()) {
            if (type.isInstance(comp)) res.add(type.cast(comp));
            if (comp instanceof Container) res.addAll(chercher((Container) comp, type));
        }
        return res;
    }

    private static List<JButton> boutons(Container c, String texte) {
        List<JButton> res = new ArrayList<>();
        for (JButton b : chercher(c, JButton.class)) {
            if (texte.equals(b.getText())) res.add(b);
        }
        return res;
    }

    private static void montrer(JFrame f) { f.setVisible(true); f.toFront(); }
    private static void erreur(Component p, String msg) { JOptionPane.showMessageDialog(p, msg, "Erreur", JOptionPane.ERROR_MESSAGE); }
    private static void info(Component p, String msg)   { JOptionPane.showMessageDialog(p, msg); }
    private static void afficherTexte(Component p, String titre, String texte) {
        JTextArea z = new JTextArea(texte, 10, 40);
        z.setEditable(false);
        JOptionPane.showMessageDialog(p, new JScrollPane(z), titre, JOptionPane.INFORMATION_MESSAGE);
    }
}