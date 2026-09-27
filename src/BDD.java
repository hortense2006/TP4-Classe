public class BDD
{
    public boolean verifierIdentifiants(String mail, String mdp) {
        String mailOriginal = "galtier.hortense@gmail.com";
        String mdpOriginal = "connexion";
        if (!mail.equals(mailOriginal) || !mdp.equals(mdpOriginal)) {
            System.out.println("Erreur identification !");
            return false;
        } else {
            System.out.println("Identifiants validés !");
            return true;
        }
    }
}
