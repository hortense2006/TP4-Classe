public class Accessoire extends Article
{
    // ATTRIBUTS
    private String dimensions;
    private String typeAccessoire;

    // CONSTRUCTEUR
    public Accessoire(String titre, float prix, String etat,String couleur, String photo)
    {
        super(titre, prix, etat, couleur, photo);
        this.dimensions = "Non renseignées";
        this.typeAccessoire = "Non renseigné";
    }
    @Override
    public String genererReference()
    {
        return "ACC-" + this.idArticle;
    }
    @Override
    public float calculerFraisPort()
    {
        return 2.99f;
    }

}
