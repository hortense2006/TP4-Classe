public interface IGestion {
    public boolean supprimerAnnonce(Article article) throws ArticleNonTrouveException;
    public void publierAnnonce(Article article);
}