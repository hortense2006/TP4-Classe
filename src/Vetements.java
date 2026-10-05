public class Vetements extends Article
{
    // ATTRIBUTS
    private String taille;
    private String matiere;

    // CONSTRUCTEUR
    public Vetements(String titre,float prix, String etat, String couleur, String photo)
    {
        super(titre, prix, etat, couleur, photo);
        this.taille = "Non renseignée";
        this.matiere = "Non renseignee";
    }

    @Override
    public String genererReference()
    {
        return "VET-" + this.idArticle;
    }

    @Override
    public float calculerFraisPort()
    {
        return 4.99f;
    }

}
