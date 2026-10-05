import java.util.ArrayList;
import java.util.List;

public class Vendeur extends Utilisateur implements IGestion {
    private float evaluation;
    private int nbVente;
    private List<Article> mesArticles;

    public Vendeur() {
        super();
        this.evaluation = 0.0f;
        this.nbVente = 0;
        this.mesArticles = new ArrayList<>();
    }

    public Vendeur(int idUtilisateur, String nom, String email, float evaluation, int nbVente) {
        super(idUtilisateur, nom, email);
        this.evaluation = evaluation;
        this.nbVente = nbVente;
        this.mesArticles = new ArrayList<>();
    }

    @Override
    public void communiquer(Utilisateur destinataire, String message) {
        System.out.println("Message de " + this.nom + " envoyé à " + destinataire.getNom() + " : " + message);
    }

    @Override
    public void bloquer(Utilisateur utilisateur) {
        System.out.println("L'utilisateur " + utilisateur.getNom() + " a été bloqué par " + this.nom);
    }

    @Override
    public void publierAnnonce(Article unArticle) {
        mesArticles.add(unArticle);
        System.out.println("Annonce publiée pour l'article : " + unArticle.getTitre());
    }

    @Override
    public boolean supprimerAnnonce(Article unArticle) throws ArticleNonTrouveException {
        if (!mesArticles.contains(unArticle)) {
            throw new ArticleNonTrouveException("Impossible de supprimer : l'article n'existe pas dans la liste du vendeur.");
        }
        mesArticles.remove(unArticle);
        System.out.println("Annonce supprimée.");
        return true;
    }

    public void vendre(Article unArticle) throws ArticleNonTrouveException {
        if (!mesArticles.contains(unArticle)) {
            throw new ArticleNonTrouveException("Vente impossible : l'article ne figure pas dans votre inventaire.");
        }
        unArticle.marqueVendu();
        mesArticles.remove(unArticle);
        this.nbVente++;

        mesArticles.add(unArticle);
        GestionFichier.enregistrerArticles(mesArticles); //enregistrer les infos(méthode qui lit)
        System.out.println("Afficher les infos de l'article");
        List<Article> articlesRecuperes = GestionFichier.recupererArticles(); //ENREGISTRE les infos (methode qui recupere)
        GestionFichier.afficherArticlesExtraits(articlesRecuperes); // AFFICHE les infos (methode qui ecrit)
    }
}