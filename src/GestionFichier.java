import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException; // classes necessaires
import java.util.ArrayList;
import java.util.List; // structure necessaire


public class GestionFichier {
    private static final String SÉPARATEUR = ";";
    private static final String CHEMIN_FICHIER = "articles.txt";


    public static void enregistrerArticles(List<Article> articles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHEMIN_FICHIER))) {
            for (Article article : articles) {
                writer.write(article.exporterFormatTexte());
                writer.newLine();
            }
            System.out.println(" Enregistrement réussi dans " + CHEMIN_FICHIER);
        } catch (IOException e) {
            System.err.println(" Écriture impossible : " + e.getMessage());
        }
    } // parcourt chaque objet article et recupere la chaine de caractere qui le caracterise
    // ecrit la chaine de caractere



    public static List<Article> recupererArticles() {
        List<Article> listeExtraite = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CHEMIN_FICHIER))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.trim().isEmpty()) continue; // lit les lignes un par un

                // Extraction des données avec le séparateur ";"
                String[] elements = ligne.split(SÉPARATEUR);

                if (elements.length >= 6) {
                    String type = elements[0];
                    String titre = elements[1];
                    float prix = Float.parseFloat(elements[2]);
                    String etat = elements[3];
                    String couleur = elements[4];
                    String photo = elements[5]; // récupère séparement toute les données de l article

                    // Instanciation de l'objet correspondant selon le type
                    if ("VETEMENT".equalsIgnoreCase(type)) {
                        listeExtraite.add(new Vetements(titre, prix, etat, couleur, photo));
                    } else if ("ACCESSOIRE".equalsIgnoreCase(type)) {
                        listeExtraite.add(new Accessoire(titre, prix, etat, couleur, photo));
                    }
                }
            }
            System.out.println(" Lecture du fichier terminée.");
        } catch (IOException e) {
            System.out.println(" Lecture impossible : " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(" Format de prix invalide dans le fichier.");
        }

        return listeExtraite; //
    }

    public static void afficherArticlesExtraits(List<Article> articles) {
        System.out.println("données extraites du fichier texte : ");

        if (articles.isEmpty()) {
            System.out.println("Aucun article trouvé.");
            return;
        } // si la liste est vide retourne un message

        for (Article article : articles) {
            article.afficherDetails();
            System.out.println("Référence : " + article.genererReference());
            System.out.println("Frais de port : " + article.calculerFraisPort() + " €");
        } //parcourt la liste d'articles et appelle la méthode d'affichage
    }
}
}
