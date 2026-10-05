import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class GestionSerialization {
    // SERIALISATION : objets -> fichier .ser
    public static void sauvegarder(List<Article> articles, String fichier) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(articles);
            System.out.println(articles.size() + " article(s) sérialisé(s) dans " + fichier);
        } catch (IOException e) {
            System.out.println("Erreur de sérialisation : " + e.getMessage());
        }
    }

    // DESERIALISATION : fichier .ser -> objets
    @SuppressWarnings("unchecked")
    public static List<Article> charger(String fichier) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {
            List<Article> articles = (List<Article>) ois.readObject();
            System.out.println(articles.size() + " article(s) désérialisé(s) depuis " + fichier);
            return articles;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur de désérialisation : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // AFFICHAGE
    public static void afficher(List<Article> articles) {
        for (Article a : articles) {
            a.afficherDetails();
            System.out.println("Référence : " + a.genererReference());
            System.out.println("-----");
        }
    }
}
