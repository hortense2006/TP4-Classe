import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * FENÊTRE 1 : "Catalogue" (page d'accueil du wireframe).
 * Affiche les Article du TP4 (Vetements / Accessoire) sous forme de cartes
 * dans une grille de 2 colonnes, avec une barre de filtres au-dessus.
 *
 * Structure :  JFrame
 *                └─ contentPane (BorderLayout)
 *                     ├─ NORTH  : barre de navigation + barre de filtres
 *                     └─ CENTER : grille de cartes dans un JScrollPane
 */
public class FenetreCatalogue extends JFrame {

    private final List<Article> articles;   // données venant du modèle (TP4)

    public FenetreCatalogue(List<Article> articles) {
        super("Catalogue");                 // titre de la fenêtre (constructeur de JFrame)
        this.articles = articles;

        setContentPane(creerContenu());     // on construit toute l'interface
        setSize(540, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fermer = quitter l'appli
    }

    /** Assemble les 2 zones de la page. */
    JPanel creerContenu() {
        JPanel racine = new JPanel(new BorderLayout());
        racine.setBackground(StyleUI.FOND);

        // --- Zone haute : navigation + filtres ---
        JPanel haut = new JPanel(new BorderLayout());
        haut.add(StyleUI.creerBarreNavigation("Catalogue"), BorderLayout.NORTH);
        haut.add(creerBarreFiltres(), BorderLayout.CENTER);
        racine.add(haut, BorderLayout.NORTH);

        // --- Zone centrale : grille d'articles ---
        JPanel grille = new JPanel(new GridLayout(0, 2, 10, 10)); // 0 ligne = autant que nécessaire
        grille.setBackground(StyleUI.FOND);
        grille.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (Article a : articles) {
            grille.add(creerCarte(a));      // une carte par article
        }
        JScrollPane defilement = new JScrollPane(grille);
        defilement.setBorder(null);
        defilement.getViewport().setBackground(StyleUI.FOND);
        racine.add(defilement, BorderLayout.CENTER);

        return racine;
    }

    /** Ligne "Catégorie | Filtres | Rechercher". */
    private JPanel creerBarreFiltres() {
        JPanel barre = new JPanel(new BorderLayout(8, 0));
        barre.setBackground(StyleUI.FOND);
        barre.setBorder(new EmptyBorder(10, 10, 0, 10));

        JComboBox<String> categorie = new JComboBox<>(new String[]{"Catégorie", "Vêtements", "Accessoires"});
        JComboBox<String> filtres = new JComboBox<>(new String[]{"Filtres", "Prix croissant", "Prix décroissant", "État"});
        JButton rechercher = StyleUI.creerBouton("Rechercher");

        JPanel combos = new JPanel(new GridLayout(1, 2, 8, 0));
        combos.setOpaque(false);
        combos.add(categorie);
        combos.add(filtres);

        barre.add(combos, BorderLayout.CENTER);
        barre.add(rechercher, BorderLayout.EAST);
        return barre;
    }

    /** Carte d'un article : image, titre, infos, prix et bouton. */
    private JPanel creerCarte(Article a) {
        JPanel carte = new JPanel(new BorderLayout(0, 6));
        carte.setBackground(StyleUI.CARTE);
        carte.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(StyleUI.NAV, 1), new EmptyBorder(8, 8, 8, 8)));

        // Image (ou placeholder coloré selon la couleur de l'article)
        carte.add(new ApercuImage(a.getPhoto(), a.getTitre(),
                StyleUI.couleurDepuisNom(a.getCouleur())), BorderLayout.NORTH);

        // Texte : titre + état + couleur (getters de la classe Article)
        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setOpaque(false);
        JLabel titre = new JLabel(a.getTitre());
        titre.setFont(StyleUI.POLICE_GRAS);
        JLabel details = new JLabel("<html>" + a.getEtat() + " · " + a.getCouleur() + "</html>");
        details.setFont(StyleUI.POLICE);
        infos.add(titre);
        infos.add(details);
        carte.add(infos, BorderLayout.CENTER);

        // Bas de carte : prix à gauche, bouton à droite
        JPanel bas = new JPanel(new BorderLayout());
        bas.setOpaque(false);
        JLabel prix = new JLabel(StyleUI.formaterPrix(a.getPrix()));
        prix.setFont(StyleUI.POLICE_GRAS);
        bas.add(prix, BorderLayout.WEST);
        bas.add(StyleUI.creerBouton("Ajouter au panier"), BorderLayout.EAST);
        carte.add(bas, BorderLayout.SOUTH);

        return carte;
    }
}
