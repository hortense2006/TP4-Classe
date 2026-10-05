import java.util.Scanner;

public abstract class Utilisateur implements IAction{

    // ATTRIBUTS
    private int idUtilisateur;
    protected String nom;
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
    public abstract void communiquer(Utilisateur destinataire, String message);

    @Override
    public abstract void bloquer(Utilisateur utilisateur);


    public boolean authentifier() throws AuthentificationException {
        System.out.println("Saisissez votre mail :");
        String mail = sc.next();
        System.out.println("Saisissez votre mot de passe :");
        String mdp = sc.next();
        return bdd.verifierIdentifiants(mail, mdp);
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
