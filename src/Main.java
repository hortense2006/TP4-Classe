import java.util.Scanner;
public class Main {
    public static void main(String[] args)
    {
        boolean connexion = false;
        // CREATIONS DES OBJETS
        Utilisateur user = new Utilisateur();
        Admin admin = new Admin();
        Client client = new Client();
        Vendeur vendeur = new Vendeur();
        // CREATION D'UN ARTICLE
        Article article = new Vetements("Pull",25.0f,"Très bon état","Rouge","pull.png");
        Scanner sc = new Scanner(System.in);
        System.out.println("Vous souhaitez vous connecter en tant qu'administrateur ou en tant qu'utilisateur ?");
        System.out.println("1.Administrateur\n2.Utilisateur\n");
        int premierChoix = sc.nextInt();
        while(!connexion)
        {
            if(premierChoix == 1)
            {
                //ADMINISTRATEUR
                admin.saisirIdentifiants("administrateur");
                System.out.println("Vous souhaitez :\n1. Bloquer un utilisateur\n2.Supprimer une annonce\n3.Traiter un signalement");
                int choix = sc.nextInt();
                if(choix == 1)
                {
                    admin.bloquer(user);
                }
                else if(choix == 2)
                {
                    admin.supprimerAnnonce(article);
                }
                else if(choix == 3)
                {
                    System.out.println("Entrz l'identifiant de l'utilisateur signalé :");
                    double idUser = sc.nextDouble();
                    admin.traiterSignal(idUser);
                }
                connexion = true;

            }
            else if(premierChoix == 2)
            {
                //UTILISATEUR
                user.saisirIdentifiants("utilisateur");
                user.affichageTableaudeBord();
                System.out.println("Vous souhaitez aller dans 1.Mes Ventes\n 2.Mes achats\n 3.Modifier mon profil\n 4.Vous déconnecter\n");
                int choix = sc.nextInt();
                if(choix == 1)
                {
                    // Mes ventes -> vendeur
                    System.out.println("MES VENTES\n 1. Communiquer avec un client\n2.Vendre un article\n3.Publier un article\n4.Supprimer un article");
                    int choixVente = sc.nextInt();
                    if(choixVente == 1)
                    {
                        vendeur.communiquer(client);
                    }
                    else if(choixVente == 2)
                    {
                        vendeur.vendre(article);
                    }
                    else if(choixVente == 3)
                    {
                        vendeur.publierAnnonce(article);
                    }
                    else if(choixVente == 4)
                    {
                        vendeur.supprimerAnnonce(article);
                    }
                    else
                    {
                        System.out.println("Choix invalide.");
                    }
                }
                else if(choix == 2)
                {
                    // Mes achats -> Client
                    System.out.println("MES ACHATS\n1.Acheter un article\n2.Communiquer avec un vendeur\n3.Rechercher un article\n4.Rechercher un vendeur");
                    int choixClient = sc.nextInt();
                    if(choixClient == 1)
                    {
                        client.acheter(article);
                    }
                    else if(choixClient == 2)
                    {
                        client.communiquer(vendeur);
                    }
                    else if (choixClient == 3)
                    {
                        client.rechercherArticle(article);
                    }
                    else if(choixClient == 4)
                    {
                        client.rechercherVendeur(vendeur);
                    }
                    else
                    {
                        System.out.println("Choix invalide.");
                    }
                }
                else if(choix == 3)
                {
                    user.modifierProfil();
                }
                else if(choix == 4)
                {
                    user.deconnecter();
                }
                else
                {
                    System.out.println("Choix invalide.");
                }
                connexion = true;
            }
            else
            {
                System.out.println("Erreur ! Saisissez un chiffre valide.");
                premierChoix = sc.nextInt();
            }
        }
        sc.close();
    }
}