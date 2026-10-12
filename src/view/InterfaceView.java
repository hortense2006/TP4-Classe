package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InterfaceView {

    public static void montrer(JFrame f) {
        f.setVisible(true);
        f.toFront();
    }

    public static void erreur(Component p, String msg) {
        JOptionPane.showMessageDialog(p, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component p, String msg) {
        JOptionPane.showMessageDialog(p, msg);
    }

    public static void afficherTexte(Component p, String titre, String texte) {
        JTextArea z = new JTextArea(texte, 10, 40);
        z.setEditable(false);
        JOptionPane.showMessageDialog(p, new JScrollPane(z), titre, JOptionPane.INFORMATION_MESSAGE);
    }

    /** Parcourt récursivement l'arbre de composants et renvoie ceux du type demandé, dans l'ordre. */
    public static <T extends Component> List<T> chercher(Container c, Class<T> type) {
        List<T> res = new ArrayList<>();
        for (Component comp : c.getComponents()) {
            if (type.isInstance(comp)) res.add(type.cast(comp));
            if (comp instanceof Container) res.addAll(chercher((Container) comp, type));
        }
        return res;
    }

    public static List<JButton> boutons(Container c, String texte) {
        List<JButton> res = new ArrayList<>();
        for (JButton b : chercher(c, JButton.class)) {
            if (texte.equals(b.getText())) res.add(b);
        }
        return res;
    }
}