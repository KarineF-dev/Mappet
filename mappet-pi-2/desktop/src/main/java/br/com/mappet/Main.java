package br.com.mappet;

import br.com.mappet.view.JanelaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Ponto de entrada do MAPPET Desktop (MVP). */
public class Main {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");   // texto suavizado
        System.setProperty("swing.aatext", "true");
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        SwingUtilities.invokeLater(() -> new JanelaPrincipal().setVisible(true));
    }
}
