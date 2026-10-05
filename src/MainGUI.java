import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Point d'entrée de l'interface graphique (le Main console du TP4 reste inchangé).
 * Crée quelques Article du TP4 et ouvre les 3 fenêtres côte à côte.
 */
public class MainGUI {

    /** Articles de démonstration (mêmes que dans le wireframe). */
    static List<Article> creerArticlesDemo() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Vetements("T-shirt Vintage", 15.00f, "Très bon état", "Bleu", "tshirt.png"));
        articles.add(new Accessoire("Baskets de Sport", 60.00f, "Bon état", "Bleu", "baskets.png"));
        articles.add(new Vetements("Veste en Jean", 35.00f, "Bon état", "Bleu", "veste.png"));
        articles.add(new Accessoire("Sac à main", 45.00f, "Très bon état", "Marron", "sac.png"));
        return articles;
    }

    public static void main(String[] args) {
        Interface.main(args);
        StyleUI.initialiser();

        // Toute création de fenêtre Swing doit se faire dans l'Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            List<Article> catalogue = creerArticlesDemo();

            // Le panier de démo contient le T-shirt et la veste
            List<Article> panier = new ArrayList<>();
            panier.add(catalogue.get(0));
            panier.add(catalogue.get(2));

            FenetreCatalogue fenCatalogue = new FenetreCatalogue(catalogue);
            fenCatalogue.setLocation(20, 20);
            fenCatalogue.setVisible(true);          // une fenêtre est invisible tant qu'on ne l'affiche pas

            FenetrePanier fenPanier = new FenetrePanier(panier);
            fenPanier.setLocation(580, 20);
            fenPanier.setVisible(true);

            FenetreVente fenVente = new FenetreVente();
            fenVente.setLocation(300, 140);
            fenVente.setVisible(true);
        });
    }
}
