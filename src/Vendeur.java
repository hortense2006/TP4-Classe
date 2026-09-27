import java.util.ArrayList;
import java.util.List;

public class Vendeur extends Utilisateur implements IGestion{

    // ATTRIBUTS
    private float evaluation;
    private int nbVente;
    private List<Article> mesArticles;

    // CONSTRUCTEURS

    public Vendeur()
    {
        super();
        this.evaluation = 0;
        this.nbVente = 0;
        this.mesArticles = new ArrayList<Article>();
    }
    public Vendeur(int idUtilisateur, String nom, String email, float evaluation, int nbVente)
    {
        super(idUtilisateur, nom, email);
        this.evaluation = evaluation;
        this.nbVente = nbVente;
        this.mesArticles = new ArrayList<>();
    }

    // METHODES

    public void communiquer( Client unClient)
    {
        System.out.println(unClient.getNom() +"vous a envoyé un message.");
    }
    public void vendre(Article unArticle)
    {
        if(mesArticles.contains(unArticle))
        {
            unArticle.marqueVendu();
            mesArticles.remove(unArticle);
            nbVente++;

            System.out.println("L'article " + unArticle.getTitre() + " a été vendu.");
        }
        else
        {
            System.out.println("Cet article n'appartient pas au vendeur.");
        }
    }
    public void publierAnnonce(Article unArticle)
    {
        mesArticles.add(unArticle);

        System.out.println("L'article " + unArticle.getTitre() + " a été publié.");
    }
    public void supprimerAnnonce(Article unArticle)
    {
        if(mesArticles.contains(unArticle))
        {
            mesArticles.remove(unArticle);

            System.out.println("L'article " + unArticle.getTitre()
                    + " a été supprimé.");
        }
        else
        {
            System.out.println("Cet article n'appartient pas au vendeur.");
        }
    }
}
