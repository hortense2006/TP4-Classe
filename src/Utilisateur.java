import java.util.Scanner;

public class Utilisateur implements IAction{

    // ATTRIBUTS
    private int idUtilisateur;
    private String nom;
    private String email;
    Scanner sc = new Scanner(System.in);
    private BDD bdd = new BDD();

    // CONSTRUCTEURS

    public Utilisateur()
    {
        this.idUtilisateur = 0;
        this.nom = "Nom inconnu";
        this.email = "inconnu@gmail.com";
    }

    public Utilisateur(int idUtilisateur, String nom, String email)
    {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.email = email;
    }

    // METHODES
    @Override
    public void communiquer(Utilisateur destinataire, String message)
    {
        System.out.println("Message envoyé à "
                + destinataire.getNom() + " : " + message);
    }

    @Override
    public void bloquer(Utilisateur utilisateur)
    {
        System.out.println("L'utilisateur "
                + utilisateur.getNom() + " a été bloqué.");
    }
    boolean authentifie = false;
    public void saisirIdentifiants(String personne)
    {
        while(!authentifie) {
            System.out.println("Entrez votre identifiant ?");
            String mail = sc.next();
            System.out.println("Entrez votre mot de passe ?");
            String mdp = sc.next();
            authentifie = authentifier(mail,mdp,personne);
        }
    }
    public boolean authentifier(String mail, String mdp, String personne) {
        System.out.println( personne + ": authentification en cours...");
        return bdd.verifierIdentifiants(mail, mdp);
    }
    public void affichageTableaudeBord(){
        System.out.println("-------------------------------- TABLEAU DE BORD ----------------------------------");
        System.out.println("Mes ventes          Mes achats              Modifier mon profil          Déconnexion");
    }
    public void modifierProfil()
    {
        System.out.println("Modifications du profil enregistrées.");
    }
    public void deconnecter()
    {
        System.out.println(this.nom + "est déconnecté.");
    }
    public String getNom(){return this.nom;}
}
