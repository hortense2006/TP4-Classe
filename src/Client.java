import java.util.ArrayList;
import java.util.List;

public class Client extends Utilisateur {
    private String adresse;
    private double soldeP;
    private String paiement;
    private Panier panier;
    private List<Article> achats = new ArrayList<>();

    public Client() {
        super();
        this.adresse = "Inconnue";
        this.soldeP = 0.0;
        this.paiement = "Carte";
        this.panier = new Panier(1, "2026-01-01");
        this.achats = new ArrayList<>();
    }

    public Client(int idUtilisateur, String nom, String email, String adresse, double soldeP, String paiement) {
        super(idUtilisateur, nom, email);
        this.adresse = adresse;
        this.soldeP = soldeP;
        this.paiement = paiement;
        this.panier = new Panier(idUtilisateur, "2026-01-01");
    }

    @Override
    public void communiquer(Utilisateur destinataire, String message) {
        System.out.println("Message de " + this.nom + " envoyé à " + destinataire.getNom() + " : " + message);
    }

    @Override
    public void bloquer(Utilisateur utilisateur) {
        System.out.println("L'utilisateur " + utilisateur.getNom() + " a été bloqué par " + this.nom);
    }

    public void acheter(Article unArticle) {
        System.out.println("Achat réalisé pour l'article : " + unArticle.getTitre());
        // On créé une liste d'articles
        achats.add(unArticle);
        GestionSerialization.sauvegarder(achats, "achats.ser");
        System.out.println("---------- AFFICHER LES INFORMATIONS DE L'ARTICLE ACHETE ---------------");
        List<Article> recuperes = GestionSerialization.charger("achats.ser");
        GestionSerialization.afficher(recuperes);
    }

    public List<Article> rechercher(String motCle) {
        System.out.println("Recherche d'articles contenant : " + motCle);
        return new ArrayList<>();
    }

    public Panier getPanier() {
        return this.panier;
    }
}