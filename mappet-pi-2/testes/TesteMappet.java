import br.com.mappet.util.*;
import br.com.mappet.controller.*;
import br.com.mappet.model.*;
import java.io.*;
import java.util.*;

public class TesteMappet {
    static int ok = 0, falha = 0;
    static void reg(String id, String desc, String esperado, String obtido) {
        boolean pass = obtido.contains(esperado);
        if (pass) ok++; else falha++;
        System.out.println(id + "|" + desc + "|" + esperado + "|" + obtido.replace("\n"," ") + "|" + (pass ? "Aprovado" : "Reprovado"));
    }
    static String erros(java.util.function.Consumer<List<String>> f) { List<String> e = new ArrayList<>(); f.accept(e); return e.isEmpty() ? "sem erros" : String.join("; ", e); }

    public static void main(String[] a) throws Exception {
        String dir = a[0];
        reg("CT01", "Nome com números no cadastro (UC01 E1)", "não pode conter números", erros(e -> Validador.textoSemNumeros(e, "Ana 2", "O nome", 100)));
        reg("CT02", "Nome só com espaços (UC01 E1)", "é obrigatório", erros(e -> Validador.textoSemNumeros(e, "   ", "O nome", 100)));
        reg("CT03", "Nome válido com acento (UC01)", "sem erros", erros(e -> Validador.textoSemNumeros(e, "Bia Lima de Sá", "O nome", 100)));
        reg("CT04", "E-mail sem @ (UC01 E1)", "Informe um e-mail válido", erros(e -> Validador.email(e, "ana.gmail.com")));
        reg("CT05", "E-mail válido (UC01)", "sem erros", erros(e -> Validador.email(e, "ana@exemplo.com")));
        reg("CT06", "Telefone com letras ou traço (UC01 E1)", "apenas números", erros(e -> Validador.telefone(e, "11 9999-0000")));
        reg("CT07", "Telefone com mais de 15 dígitos (MLR)", "apenas números", erros(e -> Validador.telefone(e, "1234567890123456")));
        reg("CT08", "Telefone só com números (UC01)", "sem erros", erros(e -> Validador.telefone(e, "11999990000")));
        reg("CT09", "Senha vazia (UC01 E1, UC02 E1)", "A senha é obrigatória", erros(e -> Validador.senha(e, "")));
        reg("CT10", "Bairro com números (UC08 E2)", "não pode conter números", erros(e -> Validador.textoSemNumeros(e, "Ipiranga 3", "O bairro", 80)));
        reg("CT11", "Nome do pet acima de 60 caracteres (MLR)", "no máximo 60", erros(e -> Validador.textoSemNumeros(e, "a".repeat(61), "O nome do pet", 60)));
        reg("CT12", "Descrição só com espaços (UC10 E1)", "A descrição é obrigatória", erros(e -> Validador.obrigatorio(e, "  ", "A descrição")));
        // fotos
        reg("CT13", "Foto PNG válida (UC07)", ".png", foto(dir + "/pet.png"));
        reg("CT14", "Foto JPG válida (UC07)", ".jpg", foto(dir + "/pet.jpg"));
        reg("CT15", "Arquivo de texto renomeado para .jpg (UC07 E1)", "JPG ou PNG", foto(dir + "/falso.jpg"));
        // CT16: cria na hora um arquivo de 6 MB, para não guardar uma imagem grande no repositório
        File grande = File.createTempFile("grande", ".png");
        grande.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(grande)) {
            out.write(new byte[] {(byte) 0x89, 'P', 'N', 'G'});
            out.write(new byte[6 * 1024 * 1024]);
        }
        reg("CT16", "Foto PNG maior que 5 MB (UC07 E1)", "no máximo 5 MB", foto(grande.getPath()));
        reg("CT17", "Publicar sem foto (UC08 E1)", "A foto é obrigatória", foto(null));
        // controllers sem login
        Sessao.sair();
        reg("CT18", "Publicar pet sem login (UC05 pré-condição)", "Faça login", tenta(() -> new PublicacaoController().publicar("perdido", "Rex", null, "Ipiranga", null)));
        reg("CT19", "Abrir área de adoção sem login (UC09 A2)", "Faça login", tenta(() -> new AdocaoController().listar()));
        reg("CT20", "Disponibilizar sem login (UC10)", "Faça login", tenta(() -> new AdocaoController().disponibilizar("Nina", null, "Ipiranga", "Dócil", null)));
        // com login, formulário vazio
        Usuario u = new Usuario("Bia Lima", "bia@exemplo.com", "11999990000", "x"); Sessao.entrar(u);
        reg("CT21", "Publicar com todos os campos vazios (UC08 E1)", "O nome do pet é obrigatório.; Escolha uma espécie da lista.; O bairro é obrigatório.; A foto é obrigatória.", tenta(() -> new PublicacaoController().publicar("perdido", "", null, "", null)));
        reg("CT22", "Disponibilizar sem descrição e sem foto (UC10 E1)", "A descrição é obrigatória", tenta(() -> new AdocaoController().disponibilizar("Nina", null, "Ipiranga", " ", null)));
        Sessao.sair();
        reg("CT23", "Sair da conta encerra a sessão (UC02 A2)", "false", String.valueOf(Sessao.estaLogado()));
        System.out.println("TOTAL|" + ok + " aprovados|" + falha + " reprovados");
    }
    interface Acao { Object run() throws Exception; }
    static String tenta(Acao a) { try { a.run(); return "sem erro"; } catch (ValidacaoException e) { return String.join("; ", e.getErros()); } catch (Exception e) { return e.getClass().getSimpleName() + ": " + e.getMessage(); } }
    static String foto(String p) { try { return FotoUtil.validar(p == null ? null : new File(p)); } catch (ValidacaoException e) { return String.join("; ", e.getErros()); } }
}
