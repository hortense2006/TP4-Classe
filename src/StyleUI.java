import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Locale;

/**
 * Classe utilitaire : couleurs, polices et petits composants réutilisés
 * par toutes les fenêtres (même charte graphique que le wireframe).
 * Aucune fenêtre n'est créée ici : on factorise juste le code répétitif.
 */
public class StyleUI {

    // ----- Charte graphique du wireframe (vert-gris) -----
    public static final Color FOND       = new Color(0xA3BAB7); // fond des fenêtres
    public static final Color NAV        = new Color(0x3F6B6E); // barre de navigation
    public static final Color NAV_ACTIF  = new Color(0x2B4F52); // onglet de la page courante
    public static final Color BOUTON     = new Color(0x2F5D62); // boutons d'action
    public static final Color CARTE      = new Color(0xDCE6E4); // cartes / lignes claires
    public static final Color TEXTE      = new Color(0x1E2D2F);

    public static final Font POLICE      = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font POLICE_GRAS = new Font("SansSerif", Font.BOLD, 12);
    public static final Font POLICE_TITRE = new Font("SansSerif", Font.BOLD, 18);

    /** À appeler une seule fois au démarrage : look & feel identique sur Windows / Mac / Linux. */
    public static void initialiser() {
        try {
            // "Metal" respecte setBackground() sur les boutons (le look natif Mac l'ignore)
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Look and feel par défaut conservé : " + e.getMessage());
        }
    }

    /** Bouton d'action sombre avec texte blanc (ex : "Ajouter au panier", "Payer"). */
    public static JButton creerBouton(String texte) {
        JButton b = new JButton(texte);
        b.setBackground(BOUTON);
        b.setForeground(Color.WHITE);
        b.setFont(POLICE_GRAS);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(5, 12, 5, 12));
        return b;
    }

    /**
     * Barre de navigation commune à toutes les pages (haut de la fenêtre).
     * @param pageActive nom de l'onglet à mettre en évidence ("Catalogue", "Vendre", "Panier"...)
     * Chaque bouton a une "actionCommand" = son nom : pratique pour les listeners du TP6 Q4.
     */
    public static JPanel creerBarreNavigation(String pageActive) {
        JPanel barre = new JPanel(new BorderLayout());
        barre.setBackground(NAV);

        JPanel gauche = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        gauche.setOpaque(false);
        droite.setOpaque(false);

        for (String nom : new String[]{"Catalogue", "Vendre"}) {
            gauche.add(creerOnglet(nom, nom.equals(pageActive)));
        }
        for (String nom : new String[]{"Panier", "Compte", "Admin"}) {
            droite.add(creerOnglet(nom, nom.equals(pageActive)));
        }
        barre.add(gauche, BorderLayout.WEST);
        barre.add(droite, BorderLayout.EAST);
        return barre;
    }

    private static JButton creerOnglet(String nom, boolean actif) {
        JButton b = new JButton(nom);
        b.setActionCommand(nom);
        b.setFont(actif ? POLICE_GRAS : POLICE);
        b.setForeground(Color.WHITE);
        b.setBackground(actif ? NAV_ACTIF : NAV);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 14, 8, 14));
        return b;
    }

    /** Formate un prix à la française : 15.0f -> "15,00 €". */
    public static String formaterPrix(float prix) {
        return String.format(Locale.FRANCE, "%.2f €", prix);
    }

    /** Traduit le champ "couleur" d'un Article (String) en vraie couleur Java. */
    public static Color couleurDepuisNom(String nom) {
        if (nom == null) return Color.GRAY;
        switch (nom.toLowerCase()) {
            case "rouge":  return new Color(0xC0392B);
            case "bleu":   return new Color(0x5B7C99);
            case "vert":   return new Color(0x4F8A5B);
            case "noir":   return new Color(0x2C2C2C);
            case "marron": return new Color(0xA0653A);
            default:       return Color.GRAY;
        }
    }
}
