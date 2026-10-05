import java.util.List;
import java.util.ArrayList;

public class Panier implements Comparable<Panier>
{
    // ATTRIBUTS
    private int idPanier;
    private String dateCreation;
    private List<Article> articles;

    //CONSTRUCTEUR
    public Panier(int idPanier, String dateCreation)
    {
        this.idPanier = idPanier;
        this.dateCreation = dateCreation;
        this.articles = new ArrayList<>();
    }
    // METHODES
    public void ajouterArticle(Article unArticle)
    {
        articles.add(unArticle);
        System.out.println("L'article " + unArticle.getTitre() + " a été ajouté au panier.");
    }

    public void retirerArticle(Article unArticle) throws ArticleNonTrouveException {
        if (!articles.contains(unArticle)) {
            throw new ArticleNonTrouveException("Cet article n'est pas présent dans votre panier.");
        }
        articles.remove(unArticle);
        System.out.println("L'article " + unArticle.getTitre() + " a été retiré du panier.");
    }

    public float calculerPrixTotal()
    {
        float total = 0;

        for(Article article : articles)
        {
            total += article.getPrix();
        }

        return total;
    }

    @Override
    public int compareTo(Panier autrePanier)
    {
        return Float.compare(this.calculerPrixTotal(), autrePanier.calculerPrixTotal()
        );
    }
}