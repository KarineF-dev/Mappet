package br.com.mappet.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Card com foto e informações de um pet, usado na Home e na vitrine da tela de login. */
public class CartaoPet extends PainelArredondado {

    private final FotoArredondada foto;

    public CartaoPet(String fotoUrl, int larguraFoto, int alturaFoto, String titulo, String... linhas) {
        super(22, Color.WHITE, Estilo.BORDA);
        setSombra(true);
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 16, 10));

        foto = new FotoArredondada(fotoUrl, larguraFoto, alturaFoto, 18);
        add(foto, BorderLayout.NORTH);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        JLabel t = Estilo.titulo(titulo, 28f, Estilo.TITULO_COR);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        textos.add(t);
        textos.add(Box.createVerticalStrut(2));
        for (String linha : linhas) {
            JLabel l = Estilo.rotulo(linha, Estilo.NEGRITO.deriveFont(13f), Estilo.CINZA);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            textos.add(l);
        }
        add(textos, BorderLayout.CENTER);
    }

    /** Selo colorido sobre a foto, por exemplo PERDIDO ou ENCONTRADO. */
    public void adicionarSelo(String texto, Color fundo, Color cor) {
        foto.setSelo(texto, fundo, cor);
    }

    /** Deixa o card inteiro clicável. */
    public void aoClicar(Runnable acao) {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { acao.run(); }
        });
    }
}
