public class BDD {
    public boolean verifierIdentifiants(String mail, String mdp) throws AuthentificationException {
        String mailOriginal = "galtier.hortense@gmail.com";
        String mdpOriginal = "connexion";

        if (!mail.equals(mailOriginal) || !mdp.equals(mdpOriginal)) {
            throw new AuthentificationException("Erreur identification : Identifiants incorrects !");
        }

        System.out.println("Identifiants validés !");
        return true;
    }
}