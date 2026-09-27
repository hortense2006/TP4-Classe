import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean connexion = false;

        Admin admin = new Admin();
        Client client = new Client();
        Vendeur vendeur = new Vendeur();

        Article article = new Vetements("Pull", 25.0f, "Très bon état", "Rouge", "pull.png");

        Scanner sc = new Scanner(System.in);

        System.out.println("Vous souhaitez vous connecter en tant qu'administrateur ou en tant qu'utilisateur ?");
        System.out.println("1. Administrateur\n2. Utilisateur\n");

        try {
            int premierChoix = sc.nextInt();

            while (!connexion) {
                if (premierChoix == 1) {
                    admin.authentifier();

                    System.out.println("\nVous souhaitez : \n1. Bloquer un utilisateur\n2. Supprimer une annonce\n3. Traiter un signalement");
                    int choix = sc.nextInt();

                    if (choix == 1) {
                        admin.bloquer(client);
                    } else if (choix == 2) {
                        admin.supprimerAnnonce(article);
                    } else if (choix == 3) {
                        System.out.println("Entrez l'identifiant de l'utilisateur signalé :");
                        int idUser = sc.nextInt();
                        admin.traiterSignal(idUser);
                    }
                    connexion = true;

                } else if (premierChoix == 2) {
                    client.authentifier();

                    System.out.println("\nVous souhaitez aller dans :\n1. Mes Ventes\n2. Mes achats\n3. Modifier mon profil\n4. Vous déconnecter\n");
                    int choix = sc.nextInt();

                    if (choix == 1) {
                        System.out.println("MES VENTES\n1. Communiquer avec un client\n2. Vendre un article\n3. Publier un article\n4. Supprimer un article\n5. Modifier prix");
                        int choixVente = sc.nextInt();

                        if (choixVente == 1) {
                            vendeur.communiquer(client, "Bonjour");
                        } else if (choixVente == 2) {
                            vendeur.vendre(article);
                        } else if (choixVente == 3) {
                            vendeur.publierAnnonce(article);
                        } else if (choixVente == 4) {
                            vendeur.supprimerAnnonce(article);
                        } else if (choixVente == 5) {
                            System.out.println("Nouveau prix :");
                            float p = sc.nextFloat();
                            article.modifierPrix(p);
                        }

                    } else if (choix == 2) {
                        System.out.println("MES ACHATS\n1. Acheter un article\n2. Communiquer avec un vendeur\n3. Retirer un article du panier");
                        int choixClient = sc.nextInt();

                        if (choixClient == 1) {
                            client.acheter(article);
                        } else if (choixClient == 2) {
                            client.communiquer(vendeur, "Est-ce disponible ?");
                        } else if (choixClient == 3) {
                            client.getPanier().retirerArticle(article);
                        }

                    } else if (choix == 3) {
                        client.modifierProfil();
                    } else if (choix == 4) {
                        client.deconnecter();
                    }
                    connexion = true;

                } else {
                    System.out.println("Erreur. Saisissez 1 ou 2 :");
                    premierChoix = sc.nextInt();
                }
            }
        } catch (AuthentificationException e) {
            System.out.println("[ERREUR AUTHENTIFICATION] " + e.getMessage());
        } catch (ArticleNonTrouveException e) {
            System.out.println("[ERREUR ARTICLE] " + e.getMessage());
        } catch (PrixInvalideException e) {
            System.out.println("[ERREUR PRIX] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Une erreur inattendue est survenue : " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}