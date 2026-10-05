public interface IAction {
    public void communiquer(Utilisateur destinataire, String message);
    public void bloquer(Utilisateur utilisateur);
}
