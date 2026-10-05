import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * FENÊTRE 3 : "Mon Panier".
 * Une ligne par article (image, titre, prix, quantité, bouton supprimer)
 * puis le récapitulatif : sous-total + frais de port + total.
 *
 * POLYMORPHISME (TP4) : les frais de port viennent de calculerFraisPort(),
 * méthode abstraite d'Article redéfinie différemment dans Vetements (4,99 €)
 * et Accessoire (2,99 €). Ici on ne se préoccupe pas du type exact.
 */
public class FenetrePanier extends JFrame {

    private final List<Article> articles;
    private final JButton btnPayer = StyleUI.creerBouton("Payer");

    public FenetrePanier(List<Article> articles) {
        super("Mon Panier");
        this.articles = articles;
        setContentPane(creerContenu());
        setSize(500, 460);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    JPanel creerContenu() {
        JPanel racine = new JPanel(new BorderLayout());
        racine.setBackground(StyleUI.FOND);
        racine.add(StyleUI.creerBarreNavigation("Panier"), BorderLayout.NORTH);

        // --- Liste des lignes (empilées verticalement) ---
        JPanel liste = new JPanel();
        liste.setLayout(new BoxLayout(liste, BoxLayout.Y_AXIS));
        liste.setBackground(StyleUI.FOND);
        liste.setBorder(new EmptyBorder(12, 12, 6, 12));
        for (Article a : articles) {
            liste.add(creerLigne(a));
            liste.add(Box.createVerticalStrut(8));   // espace entre deux lignes
        }
        JPanel conteneur = new JPanel(new BorderLayout());   // colle la liste en haut
        conteneur.setBackground(StyleUI.FOND);
        conteneur.add(liste, BorderLayout.NORTH);
        JScrollPane defilement = new JScrollPane(conteneur);
        defilement.setBorder(null);
        racine.add(defilement, BorderLayout.CENTER);

        // --- Récapitulatif + bouton Payer ---
        racine.add(creerRecapitulatif(), BorderLayout.SOUTH);
        return racine;
    }

    /** Une ligne du panier. */
    private JPanel creerLigne(Article a) {
        JPanel ligne = new JPanel(new BorderLayout(10, 0));
        ligne.setBackground(StyleUI.CARTE);
        ligne.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(StyleUI.NAV, 1), new EmptyBorder(6, 6, 6, 8)));
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));

        ApercuImage image = new ApercuImage(a.getPhoto(), a.getTitre(),
                StyleUI.couleurDepuisNom(a.getCouleur()));
        image.setPreferredSize(new Dimension(60, 60));
        ligne.add(image, BorderLayout.WEST);

        JLabel titre = new JLabel(a.getTitre());
        titre.setFont(StyleUI.POLICE_GRAS);
        ligne.add(titre, BorderLayout.CENTER);

        // Partie droite : prix + quantité (JSpinner) + corbeille
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        droite.setOpaque(false);
        droite.add(new JLabel(StyleUI.formaterPrix(a.getPrix())));
        droite.add(new JSpinner(new SpinnerNumberModel(1, 1, 99, 1))); // valeur, min, max, pas
        droite.add(StyleUI.creerBouton("Suppr."));
        ligne.add(droite, BorderLayout.EAST);

        return ligne;
    }

    /** Sous-total, frais de port, total et bouton Payer. */
    private JPanel creerRecapitulatif() {
        // Calculs à partir du modèle
        float sousTotal = 0;
        float fraisPort = 0;
        for (Article a : articles) {
            sousTotal += a.getPrix();
            fraisPort = Math.max(fraisPort, a.calculerFraisPort()); // on garde le frais le plus élevé
        }
        float total = sousTotal + fraisPort;

        JPanel recap = new JPanel(new GridLayout(3, 2, 4, 2));
        recap.setOpaque(false);
        ajouterLigneRecap(recap, "Sous-total :", StyleUI.formaterPrix(sousTotal), false);
        ajouterLigneRecap(recap, "Frais de port :", StyleUI.formaterPrix(fraisPort), false);
        ajouterLigneRecap(recap, "TOTAL :", StyleUI.formaterPrix(total), true);

        JPanel droite = new JPanel(new BorderLayout());
        droite.setOpaque(false);
        droite.setPreferredSize(new Dimension(230, 70));
        droite.add(recap, BorderLayout.CENTER);

        JPanel boutonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        boutonPanel.setOpaque(false);
        boutonPanel.add(btnPayer);

        JPanel bas = new JPanel(new BorderLayout());
        bas.setBackground(StyleUI.FOND);
        bas.setBorder(new EmptyBorder(6, 12, 12, 12));
        JPanel droiteWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        droiteWrap.setOpaque(false);
        droiteWrap.add(droite);
        bas.add(droiteWrap, BorderLayout.NORTH);
        bas.add(boutonPanel, BorderLayout.SOUTH);
        return bas;
    }

    private void ajouterLigneRecap(JPanel p, String libelle, String valeur, boolean gras) {
        JLabel l = new JLabel(libelle, SwingConstants.RIGHT);
        JLabel v = new JLabel(valeur, SwingConstants.RIGHT);
        Font f = gras ? StyleUI.POLICE_GRAS : StyleUI.POLICE;
        l.setFont(f);
        v.setFont(f);
        p.add(l);
        p.add(v);
    }
}
