public class Admin extends Utilisateur implements IGestion {
    private int nvAccreditation;
    private String role;
    private String matricule;

    public Admin() {
        super();
        this.nvAccreditation = 1;
        this.role = "Modérateur";
        this.matricule = "ADM000";
    }

    public Admin(int idUtilisateur, String nom, String email, String matricule) {
        super(idUtilisateur, nom, email);
        this.nvAccreditation = 1;
        this.role = "Modérateur";
        this.matricule = matricule;
    }

    @Override
    public void communiquer(Utilisateur destinataire, String message) {
        System.out.println("Message administrateur envoyé à " + destinataire.getNom() + " : " + message);
    }

    @Override
    public void bloquer(Utilisateur utilisateur) {
        System.out.println("L'utilisateur " + utilisateur.getNom() + " a été bloqué par l'administration.");
    }

    @Override
    public void publierAnnonce(Article unArticle) {
        System.out.println("Publication d'annonce validée par l'administrateur.");
    }

    @Override
    public boolean supprimerAnnonce(Article unArticle) throws ArticleNonTrouveException {
        System.out.println("L'administrateur a supprimé l'annonce : " + unArticle.getTitre());
        return true;
    }

    public void traiterSignal(int idUtilisateur) {
        System.out.println("Traitement du signalement concernant l'utilisateur ID : " + idUtilisateur);
    }
}