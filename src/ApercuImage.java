import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Composant graphique personnalisé : affiche la photo d'un article.
 * - si le fichier image existe (ex : "pull.png"), on l'affiche ;
 * - sinon on dessine un rectangle coloré avec l'initiale du titre (placeholder).
 * On redéfinit paintComponent(), la méthode appelée par Swing pour dessiner.
 */
public class ApercuImage extends JPanel {

    private Image image;              // null si le fichier n'existe pas
    private final String libelle;
    private final Color couleur;

    public ApercuImage(String cheminPhoto, String libelle, Color couleur) {
        this.libelle = libelle;
        this.couleur = couleur;
        setOpaque(false);
        setPreferredSize(new Dimension(100, 110));
        if (cheminPhoto != null && new File(cheminPhoto).exists()) {
            this.image = new ImageIcon(cheminPhoto).getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (image != null) {
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(couleur);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            // Initiale du titre, centrée
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 30));
            String initiale = libelle.isEmpty() ? "?" : libelle.substring(0, 1).toUpperCase();
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(initiale)) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(initiale, x, y);
        }
        g2.dispose();
    }
}
