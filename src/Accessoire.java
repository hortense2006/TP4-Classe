public class Accessoire extends Article
{
    // ATTRIBUTS
    private String dimensions;
    private String typeAccessoire;

    // CONSTRUCTEUR
    public Accessoire(String titre, float prix, String etat,String couleur, String photo)
    {
        this.titre = titre;
        this.prix = prix;
        this.etat = etat;
        this.couleur = couleur;
        this.photo = photo;
        this.dimensions = "Non renseignées";
        this.typeAccessoire = "Non renseigné";
    }

    // METHODES
    public void marqueVendu()
    {
        System.out.println("L'article " + this.titre + " a été vendu.");
    }

    public void afficherDétails()
    {
        System.out.println(this.titre);
        System.out.println("Prix : " + this.prix + " euros");
        System.out.println("Etat : " + this.etat);
        System.out.println("Couleur : " + this.couleur);
        System.out.println("Dimensions : " + this.dimensions);
        System.out.println("Type d'accessoire : " + this.typeAccessoire);
    }

    public void modifierPrix(float nvPrix)
    {
        this.prix = nvPrix;
        System.out.println("Alerte, nouveau prix de l'article " + this.titre + " : " + this.prix);
    }

    public String genererReference()
    {
        return "ACC-" + this.idArticle;
    }

    public float calculerFraisPort()
    {
        return 4.99f;
    }

    public String getTitre()
    {
        return this.titre;
    }

    public float getPrix() {
        return this.prix;
    }
}
