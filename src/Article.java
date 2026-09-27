public abstract class Article {

    // ATTRIBUTS
    protected int idArticle;
    protected String titre;
    protected float prix;
    protected String etat;
    protected String couleur;
    protected String photo;

    // METHODES
    public abstract void marqueVendu();
    public abstract void afficherDétails();
    public abstract void modifierPrix(float nvPrix);
    public abstract String genererReference();
    public abstract float calculerFraisPort();
    public abstract String getTitre();
    public abstract float getPrix();
}

