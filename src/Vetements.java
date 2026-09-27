public class Vetements extends Article
{
    // ATTRIBUTS
    private String taille;
    private String matiere;

    // CONSTRUCTEUR
    public Vetements(String titre,float prix, String etat, String couleur, String photo)
    {
        this.titre = titre;
        this.prix = prix;
        this.etat = etat;
        this.couleur = couleur;
        this.photo = photo;
        this.taille = "Non renseignée";
        this.matiere = "Non renseignée";
    }
    public void marqueVendu(){System.out.println("L'article" + this.titre + "a été vendu.");}
    public void afficherDétails()
    {
        System.out.println(this.titre);
        System.out.println("Prix" + this.prix + "euros");
        System.out.println("Etat" + this.etat);
        System.out.println("Couleur : " + this.couleur);
        System.out.println("Taille : " + this.taille);
        System.out.println("Matière : " + this.matiere);
    }
    public void modifierPrix(float nvPrix)
    {
        this.prix = nvPrix;
        System.out.println("Alerte, nouveau prix de l'article"+ this.titre+":"+this.prix);
    }
    public String genererReference()
    {
        return "VET-" + this.idArticle;
    }

    public float calculerFraisPort()
    {
        return 4.99f;
    }
    public String getTitre(){return this.titre;}
    public float getPrix(){return this.prix;}
}
