package br.com.mappet.view;

import br.com.mappet.model.Especie;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.DefaultListCellRenderer;

/**
 * Identidade visual do MAPPET Desktop, igual à da interface web:
 * azul claro no cabeçalho, laranja nos botões, fontes Mouse Memoirs (títulos) e Nunito (textos).
 * Todas as telas usam as cores, fontes e componentes daqui.
 */
public class Estilo {

    // ---------------------------------------------------------------- cores (as mesmas do CSS do site)
    public static final Color AZUL_CEU = new Color(0x96D1FA);
    public static final Color AZUL_MEDIO = new Color(0x6097D0);
    public static final Color AZUL_CLARO = new Color(0xEAF6FF);
    public static final Color TEXTO = new Color(0x31526B);
    public static final Color TITULO_COR = new Color(0x1B3E5A);
    public static final Color CINZA = new Color(0x8B929A);
    public static final Color FUNDO = new Color(0xF5FBFF);
    public static final Color CAMPO = new Color(0xF7FBFF);
    public static final Color BORDA = new Color(0xE0EDF6);
    public static final Color LARANJA = new Color(0xFF8C00);
    public static final Color LARANJA_ESCURO = new Color(0xED8100);
    public static final Color LARANJA_CLARO = new Color(0xFFE7CC);
    public static final Color VERDE_CLARO = new Color(0xEAF8F0);
    public static final Color VERDE = new Color(0x2F9E62);
    public static final Color VERMELHO = new Color(0xE03131);

    // ---------------------------------------------------------------- fontes (ficam dentro do projeto, em resources/fontes)
    private static Font carregar(String arquivo, Font reserva) {
        try (InputStream in = Estilo.class.getResourceAsStream("/fontes/" + arquivo)) {
            if (in == null) return reserva;
            Font f = Font.createFont(Font.TRUETYPE_FONT, in);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
            return f;
        } catch (Exception e) {
            return reserva;   // sem a fonte, o programa continua com a fonte padrão do Java
        }
    }

    private static final Font MOUSE = carregar("MouseMemoirs-Regular.ttf", new Font("SansSerif", Font.BOLD, 12));
    private static final Font NUNITO = carregar("Nunito-Regular.ttf", new Font("SansSerif", Font.PLAIN, 12));
    private static final Font NUNITO_BOLD = carregar("Nunito-Bold.ttf", new Font("SansSerif", Font.BOLD, 12));
    private static final Font NUNITO_EXTRA = carregar("Nunito-ExtraBold.ttf", new Font("SansSerif", Font.BOLD, 12));

    public static Font display(float tamanho) { return MOUSE.deriveFont(tamanho); }

    public static final Font TITULO = display(46f);
    public static final Font SUBTITULO = display(28f);
    public static final Font NORMAL = NUNITO.deriveFont(14f);
    public static final Font PEQUENA = NUNITO.deriveFont(12f);
    public static final Font NEGRITO = NUNITO_BOLD.deriveFont(14f);
    public static final Font ROTULO = NUNITO_BOLD.deriveFont(13f);
    public static final Font BOTAO = NUNITO_EXTRA.deriveFont(14f);
    public static final Font CHAMADA = NUNITO_EXTRA.deriveFont(12f);

    /** Liga a suavização de bordas e textos. */
    public static Graphics2D suave(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        return g2;
    }

    /** Imagem decorativa guardada em resources/imagens (gatos, logo etc.). */
    public static Image imagem(String arquivo) {
        try (InputStream in = Estilo.class.getResourceAsStream("/imagens/" + arquivo)) {
            return in == null ? null : ImageIO.read(in);
        } catch (Exception e) {
            return null;
        }
    }

    // ---------------------------------------------------------------- botões
    public static JButton botaoPrimario(String texto) {
        return new BotaoArredondado(texto, LARANJA, LARANJA_ESCURO, Color.WHITE, null);
    }

    public static JButton botaoSecundario(String texto) {
        return new BotaoArredondado(texto, Color.WHITE, AZUL_CLARO, TEXTO, AZUL_CEU);
    }

    public static JButton link(String texto) {
        JButton b = new JButton(texto);
        b.setFont(NUNITO_EXTRA.deriveFont(13f));
        b.setForeground(LARANJA);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ---------------------------------------------------------------- textos
    public static JLabel rotulo(String texto, Font fonte, Color cor) {
        JLabel l = new JLabel(texto);
        l.setFont(fonte);
        l.setForeground(cor);
        // folga à direita: com a suavização ligada a última letra às vezes ficava cortada
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
        return l;
    }

    /** Pequena chamada em laranja acima dos títulos, como "ÁREA DO USUÁRIO" no site. */
    public static JLabel chamada(String texto) {
        return rotulo(texto.toUpperCase(), CHAMADA, LARANJA);
    }

    public static JLabel titulo(String texto, float tamanho, Color cor) {
        return rotulo(texto, display(tamanho), cor);
    }

    // ---------------------------------------------------------------- cartões
    public static PainelArredondado cartao(int raio) {
        PainelArredondado p = new PainelArredondado(raio, Color.WHITE, BORDA);
        p.setSombra(true);
        return p;
    }

    public static Border bordaCartao() {
        return BorderFactory.createEmptyBorder(12, 12, 12, 12);
    }

    // ---------------------------------------------------------------- cabeçalho
    /** Barra azul do topo com o logo MAPPET, o nome da tela no centro e, à direita, os botões da tela. */
    public static JPanel cabecalho(String subtitulo, Component... direita) {
        JPanel barra = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = suave(g);
                g2.setColor(AZUL_CEU);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(0, 0, 0, 18));
                g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                g2.dispose();
            }
        };
        barra.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));
        barra.add(new Logo(), BorderLayout.WEST);
        if (subtitulo != null) {
            JLabel centro = titulo(subtitulo, 32f, Color.WHITE);
            centro.setHorizontalAlignment(SwingConstants.CENTER);
            barra.add(centro, BorderLayout.CENTER);
        }
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 17));
        botoes.setOpaque(false);
        for (Component c : direita) botoes.add(c);
        barra.add(botoes, BorderLayout.EAST);
        barra.setPreferredSize(new Dimension(100, 74));
        return barra;
    }

    /** Logo do site: "MAP" branco, "PET" laranja e o pin de localização. */
    static class Logo extends JComponent {
        Logo() { setPreferredSize(new Dimension(190, 74)); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suave(g);
            Font f = display(40f);
            g2.setFont(f);
            int base = getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 6;
            g2.setColor(Color.WHITE);
            g2.drawString("MAP", 0, base);
            int x = g2.getFontMetrics().stringWidth("MAP");
            g2.setColor(LARANJA);
            g2.drawString("PET", x, base);
            x += g2.getFontMetrics().stringWidth("PET") + 10;
            int d = 32, y = getHeight() / 2 - d / 2;
            g2.fillOval(x, y, d, d);
            g2.setColor(Color.WHITE);
            Path2D pin = new Path2D.Double();
            double cx = x + d / 2.0, cy = y + d / 2.0 - 2;
            pin.moveTo(cx, cy + 10);
            pin.curveTo(cx - 9, cy + 1, cx - 7, cy - 8, cx, cy - 8);
            pin.curveTo(cx + 7, cy - 8, cx + 9, cy + 1, cx, cy + 10);
            g2.fill(pin);
            g2.setColor(LARANJA);
            g2.fillOval((int) cx - 3, (int) cy - 4, 6, 6);
            g2.dispose();
        }
    }

    // ---------------------------------------------------------------- campos de formulário
    /** Rótulo em cima do campo; o campo fica numa caixa arredondada como no site. */
    public static JPanel campo(String rotulo, JComponent componente) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel l = rotulo(rotulo, ROTULO, TEXTO);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(6));
        JComponent caixa = caixa(componente);
        caixa.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(caixa);
        p.add(Box.createVerticalStrut(14));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    /** Coloca o componente dentro de uma caixa arredondada de fundo azul bem claro. */
    public static JComponent caixa(JComponent componente) {
        PainelArredondado caixa = new PainelArredondado(14, CAMPO, BORDA);
        caixa.setLayout(new BorderLayout());
        componente.setFont(NORMAL);
        componente.setForeground(TEXTO);
        componente.setOpaque(false);
        if (componente instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) componente;
            sp.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 8));
            sp.getViewport().setOpaque(false);
            Component dentro = sp.getViewport().getView();
            if (dentro instanceof JComponent) {
                ((JComponent) dentro).setOpaque(false);
                dentro.setFont(NORMAL);
                dentro.setForeground(TEXTO);
                ((JComponent) dentro).setBorder(BorderFactory.createEmptyBorder());
            }
            estilizarRolagem(sp);
            caixa.setPreferredSize(new Dimension(320, 100));
            caixa.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        } else {
            componente.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 10));
            caixa.setPreferredSize(new Dimension(320, 44));
            caixa.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        }
        if (componente instanceof javax.swing.text.JTextComponent) {
            ((javax.swing.text.JTextComponent) componente).setCaretColor(LARANJA);
            ((javax.swing.text.JTextComponent) componente).setSelectionColor(LARANJA_CLARO);
        }
        caixa.add(componente, BorderLayout.CENTER);
        return caixa;
    }

    /** Área de texto de várias linhas já dentro de uma rolagem, para descrições. */
    public static JScrollPane rolagemTexto(JTextArea area) {
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return new JScrollPane(area);
    }

    /** Lista de espécies com uma primeira opção vazia (id 0), como "Selecione" ou "Todas". */
    public static JComboBox<Especie> comboEspecies(List<Especie> especies, String primeiraOpcao) {
        JComboBox<Especie> combo = new JComboBox<>();
        combo.addItem(new Especie(0, primeiraOpcao));
        for (Especie e : especies) combo.addItem(e);
        estilizarCombo(combo);
        return combo;
    }

    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton seta = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = suave(g);
                        g2.setColor(TEXTO);
                        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        int cx = getWidth() / 2, cy = getHeight() / 2;
                        g2.drawLine(cx - 5, cy - 2, cx, cy + 3);
                        g2.drawLine(cx, cy + 3, cx + 5, cy - 2);
                        g2.dispose();
                    }
                };
                seta.setContentAreaFilled(false);
                seta.setBorderPainted(false);
                seta.setFocusPainted(false);
                seta.setOpaque(false);
                seta.setPreferredSize(new Dimension(34, 34));
                return seta;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                // o fundo é desenhado pela caixa arredondada
            }
        });
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setFont(NORMAL);
                l.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                l.setForeground(TEXTO);
                l.setBackground(isSelected ? AZUL_CLARO : Color.WHITE);
                l.setOpaque(index >= 0);
                return l;
            }
        });
        combo.setOpaque(false);
        combo.setBorder(BorderFactory.createEmptyBorder());
        combo.setBackground(CAMPO);
        combo.setForeground(TEXTO);
        combo.setFont(NORMAL);
        combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Devolve a espécie escolhida, ou null quando está na primeira opção (id 0). */
    public static Especie especieEscolhida(JComboBox<Especie> combo) {
        Especie e = (Especie) combo.getSelectedItem();
        return (e == null || e.getId() == 0) ? null : e;
    }

    // ---------------------------------------------------------------- rolagem
    /** Rolagem sem borda, com barra fina azul, como no site. */
    public static JScrollPane rolagem(Component conteudo) {
        JScrollPane sp = new JScrollPane(conteudo);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(18);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        estilizarRolagem(sp);
        return sp;
    }

    public static void estilizarRolagem(JScrollPane sp) {
        JScrollBar barra = sp.getVerticalScrollBar();
        barra.setOpaque(false);
        barra.setPreferredSize(new Dimension(10, 10));
        barra.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = AZUL_CEU;
                trackColor = new Color(0, 0, 0, 0);
            }

            @Override
            protected JButton createDecreaseButton(int o) { return semBotao(); }

            @Override
            protected JButton createIncreaseButton(int o) { return semBotao(); }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) { }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                if (r.isEmpty()) return;
                Graphics2D g2 = suave(g);
                g2.setColor(thumbColor);
                g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
                g2.dispose();
            }

            private JButton semBotao() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
    }

    // ---------------------------------------------------------------- mensagens
    /** Alerta de erro com a lista de campos com problema (fluxos de exceção dos casos de uso). */
    public static void alertaErros(Component pai, List<String> erros) {
        StringBuilder sb = new StringBuilder("<html>Confira os campos abaixo:<br>");
        for (String e : erros) sb.append("&nbsp;&nbsp;•&nbsp;").append(e).append("<br>");
        sb.append("</html>");
        Dialogo.mostrar(pai, Dialogo.Tipo.ALERTA, "Ops! Faltam informações", sb.toString());
    }

    public static void erroBanco(Component pai, Exception e) {
        Dialogo.mostrar(pai, Dialogo.Tipo.ERRO, "Algo deu errado",
            "<html>Não foi possível acessar o banco de dados.<br>" + e.getMessage() + "</html>");
    }

    public static void aviso(Component pai, String texto) {
        aviso(pai, "Tudo certo!", texto);
    }

    public static void aviso(Component pai, String titulo, String texto) {
        Dialogo.mostrar(pai, Dialogo.Tipo.INFO, titulo, "<html>" + texto.replace("\n", "<br>") + "</html>");
    }

    /** Margem interna padrão das áreas de conteúdo. */
    public static Insets margem() { return new Insets(24, 40, 24, 40); }
}
