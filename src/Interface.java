import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Question 4 : interface graphique Swing (programmation événementielle).
 * Pages (onglets) : Connexion -> Catalogue / Vendre / Panier (cf. StoryBoard Q2).
 * Réutilise : BDD, Client, Vendeur, Article, Panier, GestionFichier, GestionSerialization
 * et les exceptions AuthentificationException, PrixInvalideException, ArticleNonTrouveException.
 */
public class Interface {

    // Objets métier (modèle) partagés par toutes les fenêtres
    private static final Client client = new Client();
    private static final Vendeur vendeur = new Vendeur();
    private static final BDD bdd = new BDD();

    // Modèles de listes Swing (la vue se met à jour quand on modifie ces modèles)
    private static final DefaultListModel<Article> modeleCatalogue = new DefaultListModel<>();
    private static final DefaultListModel<Article> modelePanier = new DefaultListModel<>();
    private static final JLabel lblTotal = new JLabel("TOTAL : 0.00 €");


    // =====================================================================
    // PAGE 1 : CONNEXION  (listener + AuthentificationException)
    // =====================================================================
    private static void fenetreConnexion() {
        JFrame f = new JFrame("Connexion");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(4, 2, 5, 5));

        JTextField champMail = new JTextField();
        JPasswordField champMdp = new JPasswordField();
        JButton btnConnexion = new JButton("Se connecter");
        JLabel message = new JLabel(" ");

        f.add(new JLabel("Adresse e-mail :"));  f.add(champMail);
        f.add(new JLabel("Mot de passe :"));    f.add(champMdp);
        f.add(btnConnexion);                    f.add(message);

        // LISTENER : clic sur le bouton "Se connecter"
        btnConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // lève AuthentificationException si les identifiants sont faux
                    bdd.verifierIdentifiants(champMail.getText(), new String(champMdp.getPassword()));
                    f.dispose();
                    fenetrePrincipale();               // ouvre la 2e fenêtre
                } catch (AuthentificationException ex) {
                    message.setForeground(Color.RED);
                    message.setText("Identifiants incorrects");
                    JOptionPane.showMessageDialog(f, ex.getMessage(), "Erreur authentification",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        f.setSize(420, 180);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    // =====================================================================
    // FENÊTRE PRINCIPALE : onglets Catalogue / Vendre / Panier
    // =====================================================================
    private static void fenetrePrincipale() {
        JFrame f = new JFrame("Seconde main - Application");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane onglets = new JTabbedPane();
        onglets.addTab("Catalogue", pageCatalogue());
        onglets.addTab("Vendre", pageVente(onglets));
        onglets.addTab("Panier", pagePanier());

        f.add(onglets);
        f.setSize(650, 450);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    // ---------------------------------------------------------------------
    // PAGE CATALOGUE : panier, fichier texte (GestionFichier), vente
    // ---------------------------------------------------------------------
    private static JPanel pageCatalogue() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JList<Article> liste = new JList<>(modeleCatalogue);
        liste.setCellRenderer(rendu());

        JButton btnAjouter = new JButton("Ajouter au panier");
        JButton btnVendu = new JButton("Marquer vendu (écrit articles.txt)");
        JButton btnCharger = new JButton("Lire articles.txt");
        JPanel bas = new JPanel();
        bas.add(btnAjouter); bas.add(btnVendu); bas.add(btnCharger);

        p.add(new JScrollPane(liste), BorderLayout.CENTER);
        p.add(bas, BorderLayout.SOUTH);

        // LISTENER (lambda) : ajouter l'article sélectionné au panier
        btnAjouter.addActionListener(e -> {
            Article a = liste.getSelectedValue();
            if (a == null) {
                JOptionPane.showMessageDialog(p, "Sélectionnez un article.");
                return;
            }
            client.getPanier().ajouterArticle(a);   // modèle
            modelePanier.addElement(a);             // vue
            majTotal();
        });

        // LISTENER : vendre -> Vendeur.vendre() enregistre dans le fichier texte
        // puis relit et affiche le contenu (GestionFichier). Exception si l'article n'est pas publié.
        btnVendu.addActionListener(e -> {
            Article a = liste.getSelectedValue();
            if (a == null) return;
            try {
                vendeur.vendre(a);
                JOptionPane.showMessageDialog(p, "Article vendu et enregistré dans articles.txt");
            } catch (ArticleNonTrouveException ex) {
                JOptionPane.showMessageDialog(p, ex.getMessage(), "Erreur article", JOptionPane.ERROR_MESSAGE);
            }
        });

        // LISTENER : lecture du fichier texte et affichage dans une boîte de dialogue
        btnCharger.addActionListener(e -> {
            List<Article> lus = GestionFichier.recupererArticles();
            StringBuilder sb = new StringBuilder();
            for (Article a : lus) sb.append(a.exporterFormatTexte()).append("\n");
            afficherTexte(p, "Contenu lu dans articles.txt", lus.isEmpty() ? "Fichier vide ou absent." : sb.toString());
        });
        return p;
    }

    // ---------------------------------------------------------------------
    // PAGE VENTE : formulaire + PrixInvalideException
    // ---------------------------------------------------------------------
    private static JPanel pageVente(JTabbedPane onglets) {
        JPanel p = new JPanel(new GridLayout(6, 2, 5, 5));
        JTextField titre = new JTextField();
        JTextField prix = new JTextField();
        JComboBox<String> etat = new JComboBox<>(new String[]{"Neuf", "Très bon état", "Bon état", "Satisfaisant"});
        JTextField couleur = new JTextField();
        JComboBox<String> categorie = new JComboBox<>(new String[]{"Vêtements", "Accessoires"});
        JButton btnPublier = new JButton("Publier l'annonce");

        p.add(new JLabel("Titre :"));      p.add(titre);
        p.add(new JLabel("Prix (€) :"));   p.add(prix);
        p.add(new JLabel("État :"));       p.add(etat);
        p.add(new JLabel("Couleur :"));    p.add(couleur);
        p.add(new JLabel("Catégorie :"));  p.add(categorie);
        p.add(btnPublier);

        // LISTENER : publication d'une annonce
        btnPublier.addActionListener(e -> {
            try {
                float valeur = Float.parseFloat(prix.getText().replace(',', '.')); // NumberFormatException
                if (valeur <= 0) {
                    throw new PrixInvalideException("Le prix doit être strictement supérieur à 0.");
                }
                String t = titre.getText().trim();
                if (t.isEmpty()) {
                    JOptionPane.showMessageDialog(p, "Le titre est obligatoire.");
                    return;
                }
                String e1 = (String) etat.getSelectedItem();
                Article a = categorie.getSelectedIndex() == 0
                        ? new Vetements(t, valeur, e1, couleur.getText(), "photo.png")
                        : new Accessoire(t, valeur, e1, couleur.getText(), "photo.png");

                vendeur.publierAnnonce(a);        // modèle
                modeleCatalogue.addElement(a);    // vue : apparaît dans le catalogue
                titre.setText(""); prix.setText(""); couleur.setText("");
                onglets.setSelectedIndex(0);      // navigation entre pages
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(p, "Le prix doit être un nombre.", "Erreur de saisie",
                        JOptionPane.ERROR_MESSAGE);
            } catch (PrixInvalideException ex) {
                JOptionPane.showMessageDialog(p, ex.getMessage(), "Erreur prix", JOptionPane.ERROR_MESSAGE);
            }
        });
        return p;
    }

    // ---------------------------------------------------------------------
    // PAGE PANIER : retrait (ArticleNonTrouveException) + sérialisation
    // ---------------------------------------------------------------------
    private static JPanel pagePanier() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JList<Article> liste = new JList<>(modelePanier);
        liste.setCellRenderer(rendu());

        JButton btnRetirer = new JButton("Retirer");
        JButton btnValider = new JButton("Valider la commande (sérialise)");
        JButton btnAchats = new JButton("Lire achats.ser");
        JPanel bas = new JPanel();
        bas.add(lblTotal); bas.add(btnRetirer); bas.add(btnValider); bas.add(btnAchats);

        p.add(new JScrollPane(liste), BorderLayout.CENTER);
        p.add(bas, BorderLayout.SOUTH);

        // LISTENER : retirer l'article sélectionné du panier
        btnRetirer.addActionListener(e -> {
            Article a = liste.getSelectedValue();
            if (a == null) return;
            try {
                client.getPanier().retirerArticle(a);  // lève ArticleNonTrouveException
                modelePanier.removeElement(a);
                majTotal();
            } catch (ArticleNonTrouveException ex) {
                JOptionPane.showMessageDialog(p, ex.getMessage(), "Erreur article", JOptionPane.ERROR_MESSAGE);
            }
        });

        // LISTENER : validation -> Client.acheter() sérialise la liste dans achats.ser
        btnValider.addActionListener(e -> {
            if (modelePanier.isEmpty()) {
                JOptionPane.showMessageDialog(p, "Le panier est vide.");
                return;
            }
            for (int i = 0; i < modelePanier.size(); i++) {
                client.acheter(modelePanier.get(i));   // sauvegarde achats.ser
            }
            JOptionPane.showMessageDialog(p, "Commande validée.\nAchats sérialisés dans achats.ser");
            modelePanier.clear();
            majTotal();
        });

        // LISTENER : désérialisation et affichage
        btnAchats.addActionListener(e -> {
            List<Article> lus = GestionSerialization.charger("achats.ser");
            StringBuilder sb = new StringBuilder();
            for (Article a : lus) {
                sb.append(a.genererReference()).append(" | ").append(a.getTitre())
                        .append(" | ").append(a.getPrix()).append(" €\n");
            }
            afficherTexte(p, "Achats désérialisés (achats.ser)", lus.isEmpty() ? "Aucun achat." : sb.toString());
        });
        return p;
    }

    // =====================================================================
    // Utilitaires
    // =====================================================================
    private static void majTotal() {
        lblTotal.setText(String.format("TOTAL : %.2f €", client.getPanier().calculerPrixTotal()));
    }

    private static void afficherTexte(Component parent, String titre, String texte) {
        JTextArea zone = new JTextArea(texte, 10, 40);
        zone.setEditable(false);
        JOptionPane.showMessageDialog(parent, new JScrollPane(zone), titre, JOptionPane.INFORMATION_MESSAGE);
    }

    private static ListCellRenderer<Article> rendu() {
        return (list, a, index, sel, focus) -> {
            JLabel l = new JLabel(a.getTitre() + " - " + a.getPrix() + " € (" + a.getEtat()
                    + ", " + a.getCouleur() + ")");
            l.setOpaque(true);
            l.setBackground(sel ? list.getSelectionBackground() : list.getBackground());
            return l;
        };
    }
}
