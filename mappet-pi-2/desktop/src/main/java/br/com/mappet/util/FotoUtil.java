package br.com.mappet.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Foto do pet (UC07): só JPG ou PNG, até 5 MB.
 * A imagem é copiada para a pasta "fotos" com um nome gerado pelo sistema,
 * e o banco guarda apenas esse caminho (FOTO_URL).
 */
public class FotoUtil {

    public static final long TAMANHO_MAXIMO = 5L * 1024 * 1024;
    private static final String PASTA = "fotos";

    /** UC07 E1: confere o formato pelo conteúdo do arquivo (não pela extensão) e o tamanho. */
    public static String validar(File arquivo) throws ValidacaoException {
        if (arquivo == null) {
            throw new ValidacaoException("A foto é obrigatória.");
        }
        if (arquivo.length() > TAMANHO_MAXIMO) {
            throw new ValidacaoException("A foto deve ter no máximo 5 MB.");
        }
        byte[] b = new byte[4];
        try (InputStream in = new FileInputStream(arquivo)) {
            if (in.read(b) < 4) throw new ValidacaoException("A foto deve estar em JPG ou PNG.");
        } catch (IOException e) {
            throw new ValidacaoException("Não foi possível ler a foto.");
        }
        boolean jpg = (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF;
        boolean png = (b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G';
        if (jpg) return ".jpg";
        if (png) return ".png";
        throw new ValidacaoException("A foto deve estar em JPG ou PNG.");
    }

    /** Copia a foto para a pasta "fotos" com nome aleatório e devolve o caminho para o banco. */
    public static String salvar(File arquivo) throws ValidacaoException, IOException {
        String extensao = validar(arquivo);
        Path pasta = Paths.get(PASTA);
        Files.createDirectories(pasta);
        Path destino = pasta.resolve(UUID.randomUUID() + extensao);
        Files.copy(arquivo.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        return PASTA + "/" + destino.getFileName();
    }

    /** Apaga uma foto já copiada (usado quando a gravação no banco falha). */
    public static void apagar(String caminho) {
        try {
            if (caminho != null) Files.deleteIfExists(Paths.get(caminho));
        } catch (IOException ignorado) {
            // se não conseguir apagar, o arquivo só fica sobrando na pasta
        }
    }

    /** Imagem redimensionada para caber em largura x altura, mantendo a proporção. */
    public static ImageIcon icone(String caminho, int largura, int altura) {
        try {
            BufferedImage img = ImageIO.read(new File(caminho));
            if (img == null) return null;
            double escala = Math.min((double) largura / img.getWidth(), (double) altura / img.getHeight());
            int w = Math.max(1, (int) (img.getWidth() * escala));
            int h = Math.max(1, (int) (img.getHeight() * escala));
            return new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }
}
