public class Client extends Utilisateur {

    // ATTRIBUTS
    private String adresse;
    private float soldeP;
    private String paiement;
    private Panier panier;

    // CONSTRUCTEURS

    public Client()
    {
        super();
        this.adresse = "Adresse non renseignée";
        this.soldeP = 0;
        this.paiement = "Aucun de moyen de paiement enregistre";
        this.panier = new Panier(1, "22/09/2026");
    }

    public Client(int idUtilisateur, String nom, String email, String adresse, float soldeP, String paiement)
    {
        super(idUtilisateur,nom,email);
        this.adresse = adresse;
        this.soldeP = soldeP;
        this.paiement = paiement;
    }

    // METHODES
    public void acheter(Article unArticle)
    {
        System.out.println(getNom()+ "a acheté :" + unArticle.getTitre());
    }
    public void communiquer(Vendeur unVendeur)
    {
        System.out.println(unVendeur.getNom() + "vous a envoyé un message.");
    }
    public void rechercherArticle(Article unArticle)
    {
        if(unArticle != null)
        {
            System.out.println("Article trouvé : " + unArticle.getTitre());
        }
        else
        {
            System.out.println("Article introuvable.");
        }
    }
    public void rechercherVendeur(Vendeur unVendeur)
    {
        if(unVendeur != null)
        {
            System.out.println("Vendeur trouvé : " + unVendeur.getNom());
        }
        else
        {
            System.out.println("Vendeur introuvable.");
        }
    }

}
