public class Admin extends Utilisateur{

    // ATTRIBUTS
    private int nvAccreditation;
    private String role;
    private String matricule;

    // CONSTRUCTEURS

    public Admin()
    {
        super();
        this.nvAccreditation = 0;
        this.role = "Administrateur";
        this.matricule = "ADM-001";
    }
    public Admin(int idUtilisateur, String nom,String email, String matricule)
    {
        super(idUtilisateur,nom,email);
        this.nvAccreditation = 0;
        this.role = "Moderateur";
        this.matricule = matricule;
    }

    // METHODES
    public void bloquer(Utilisateur unUtilisateur)
    {
        System.out.println("L'administrateur" + getNom() + " a bloqué" + unUtilisateur.getNom());
    }
    public void supprimerAnnonce(Article unArticle)
    {
        System.out.println("L'administrateur" + getNom() + " a supprimé l'annonce" + unArticle.getTitre());
    }
    public void traiterSignal( double idUtilisateur)
    {
        System.out.println(idUtilisateur + " a été signalé.");
    }
}
