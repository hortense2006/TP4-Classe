public abstract class Article {
    protected String titre;
    protected float prix;
    protected String etat;
    protected String couleur;
    protected String photo;
    protected int idArticle;

    public Article() {
    }

    public Article(String titre, float prix, String etat, String couleur, String photo) {
        this.titre = titre;
        this.prix = prix;
        this.etat = etat;
        this.couleur = couleur;
        this.photo = photo;
    }

    public void marqueVendu() {
        System.out.println("L'article " + this.titre + " a été vendu");
    }

    public void afficherDetails() {
        System.out.println("Titre : " + this.titre);
        System.out.println("Prix : " + this.prix + " €");
        System.out.println("État : " + this.etat);
        System.out.println("Couleur : " + this.couleur);
    }

    public void modifierPrix(float nvPrix) throws PrixInvalideException {
        if (nvPrix <= 0) {
            throw new PrixInvalideException("Le prix doit être strictement supérieur à 0.");
        }
        this.prix = nvPrix;
        System.out.println("Nouveau prix de l'article : " + this.prix + " €");
    }

    public abstract String genererReference();
    public abstract float calculerFraisPort();

    public String getTitre() {
        return this.titre;
    }

    public float getPrix() {
        return this.prix;
    }
}