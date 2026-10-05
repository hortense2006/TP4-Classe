import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * FENÊTRE 2 : "Vendre un article" (formulaire du wireframe).
 * Les champs correspondent aux attributs de la classe Article du TP4
 * (titre, prix, état, couleur, photo) + le choix Vêtements / Accessoire.
 *
 * Les composants sont des ATTRIBUTS de la classe : au TP6 Q4 on pourra
 * y accéder depuis les listeners (ex : lire txtTitre quand on clique sur "Publier").
 */
public class FenetreVente extends JFrame {

    // --- Composants du formulaire ---
    private final JTextField txtTitre = new JTextField(18);
    private final JTextArea txtDescription = new JTextArea(3, 18);
    private final JTextField txtPrix = new JTextField(6);
    private final JComboBox<String> cbCategorie = new JComboBox<>(new String[]{"Vêtements", "Accessoires"});
    private final JButton btnImage = new JButton("Ajouter une image");
    private final JComboBox<String> cbEtat =
            new JComboBox<>(new String[]{"Neuf", "Très bon état", "Bon état", "Satisfaisant"});
    private final JButton btnPublier = StyleUI.creerBouton("Publier");

    public FenetreVente() {
        super("Vendre un Article");
        setContentPane(creerContenu());
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ferme cette fenêtre seulement
    }

    JPanel creerContenu() {
        JPanel racine = new JPanel(new BorderLayout());
        racine.setBackground(StyleUI.FOND);
        racine.add(StyleUI.creerBarreNavigation("Vendre"), BorderLayout.NORTH);

        // Carte blanche centrée contenant le formulaire
        JPanel formulaire = new JPanel(new GridBagLayout());
        formulaire.setBackground(Color.WHITE);
        formulaire.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(StyleUI.NAV, 1), new EmptyBorder(14, 16, 14, 16)));

        // Titre du formulaire (sur 2 colonnes)
        JLabel titre = new JLabel("Mettre en vente", SwingConstants.CENTER);
        titre.setFont(StyleUI.POLICE_TITRE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);
        formulaire.add(titre, gbc);

        // Une ligne = un libellé + un champ
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        ajouterLigne(formulaire, 1, "Titre :", txtTitre);
        ajouterLigne(formulaire, 2, "Description :", new JScrollPane(txtDescription));
        ajouterLigne(formulaire, 3, "Prix (€) :", txtPrix);
        ajouterLigne(formulaire, 4, "Catégorie :", cbCategorie);
        ajouterLigne(formulaire, 5, "Image :", btnImage);
        ajouterLigne(formulaire, 6, "État :", cbEtat);
        ajouterLigne(formulaire, 7, "Couleur :", creerChoixCouleur());

        // Bouton "Publier" centré en bas
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 0, 0, 0);
        formulaire.add(btnPublier, gbc);

        // Wrapper pour centrer la carte avec des marges
        JPanel centre = new JPanel(new GridBagLayout());
        centre.setBackground(StyleUI.FOND);
        centre.add(formulaire);
        racine.add(centre, BorderLayout.CENTER);

        return racine;
    }

    /** Ajoute "libellé + composant" sur une ligne du GridBagLayout. */
    private void ajouterLigne(JPanel p, int ligne, String libelle, JComponent champ) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = ligne;
        g.insets = new Insets(4, 0, 4, 8);
        g.anchor = GridBagConstraints.NORTHWEST;

        g.gridx = 0;
        JLabel l = new JLabel(libelle);
        l.setFont(StyleUI.POLICE_GRAS);
        p.add(l, g);

        g.gridx = 1;
        g.insets = new Insets(4, 0, 4, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        p.add(champ, g);
    }

    /** 5 pastilles de couleur : un seul choix possible (ButtonGroup). */
    private JPanel creerChoixCouleur() {
        JPanel panneau = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panneau.setOpaque(false);
        ButtonGroup groupe = new ButtonGroup();   // garantit qu'une seule pastille est sélectionnée

        String[] noms = {"Gris", "Bleu", "Vert", "Rouge", "Noir"};
        Color[] couleurs = {Color.GRAY, new Color(0x2E86C1), new Color(0x28A745),
                new Color(0xD9382E), new Color(0x2C3E50)};

        for (int i = 0; i < noms.length; i++) {
            // Sous-classe anonyme : on dessine nous-mêmes le fond coloré (le look Metal le masquerait)
            JToggleButton pastille = new JToggleButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    g.setColor(getBackground());
                    g.fillRect(0, 0, getWidth(), getHeight());
                    super.paintComponent(g);
                }
            };
            pastille.setActionCommand(noms[i]);      // on pourra récupérer la couleur choisie
            pastille.setToolTipText(noms[i]);
            pastille.setBackground(couleurs[i]);
            pastille.setOpaque(true);
            pastille.setContentAreaFilled(false);     // on désactive le remplissage standard (bleu quand sélectionné)
            pastille.setFocusPainted(false);
            pastille.setPreferredSize(new Dimension(26, 26));
            pastille.setBorder(new LineBorder(Color.DARK_GRAY, 1));
            // La sélection est visible grâce à une bordure plus épaisse
            pastille.addItemListener(e -> pastille.setBorder(
                    new LineBorder(pastille.isSelected() ? Color.BLACK : Color.DARK_GRAY,
                                   pastille.isSelected() ? 3 : 1)));
            groupe.add(pastille);
            if (i == 0) pastille.setSelected(true);   // "Gris" par défaut
            panneau.add(pastille);
        }
        return panneau;
    }
}
