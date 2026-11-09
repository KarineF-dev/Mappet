package br.com.mappet.view;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** Janela única do sistema: cada tela é um painel que substitui o anterior. */
public class JanelaPrincipal extends JFrame {

    public JanelaPrincipal() {
        super("MAPPET");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 720));
        setSize(1240, 800);
        getContentPane().setBackground(Estilo.FUNDO);
        setLocationRelativeTo(null);
        mostrarLogin();
    }

    private void trocar(JPanel tela) {
        setContentPane(tela);
        revalidate();
        repaint();
    }

    public void mostrarLogin() { trocar(new PainelLogin(this)); }
    public void mostrarCadastro() { trocar(new PainelCadastro(this)); }
    public void mostrarHome() { trocar(new PainelHome(this)); }
    public void mostrarPublicar(String tipo) { trocar(new PainelPublicar(this, tipo)); }
    public void mostrarAdocao() { trocar(new PainelAdocao(this)); }
    public void mostrarDisponibilizar() { trocar(new PainelDisponibilizar(this)); }
}
