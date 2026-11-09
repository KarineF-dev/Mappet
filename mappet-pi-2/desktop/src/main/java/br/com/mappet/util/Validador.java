package br.com.mappet.util;

import java.util.List;
import java.util.regex.Pattern;

/** Regras de validação dos casos de uso (UC01, UC02, UC08 e UC10) e tamanhos do MLR. */
public class Validador {

    private static final Pattern SO_LETRAS = Pattern.compile("^[\\p{L}\\s'.]+$");   // sem números
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern SO_NUMEROS = Pattern.compile("^\\d+$");

    /** Obrigatório, sem números, sem ficar só com espaços e até "max" caracteres (UC01, UC08). */
    public static void textoSemNumeros(List<String> erros, String valor, String campo, int max) {
        String v = valor == null ? "" : valor.trim();
        if (v.isEmpty()) {
            erros.add(campo + " é obrigatório.");
        } else if (!SO_LETRAS.matcher(v).matches()) {
            erros.add(campo + " não pode conter números.");
        } else if (v.length() > max) {
            erros.add(campo + " deve ter no máximo " + max + " caracteres.");
        }
    }

    /** E-mail em formato válido, até 150 caracteres (UC01, UC02). */
    public static void email(List<String> erros, String valor) {
        String v = valor == null ? "" : valor.trim();
        if (v.isEmpty()) {
            erros.add("O e-mail é obrigatório.");
        } else if (!EMAIL.matcher(v).matches() || v.length() > 150) {
            erros.add("Informe um e-mail válido.");
        }
    }

    /** Telefone só com números, até 15 dígitos (UC01). */
    public static void telefone(List<String> erros, String valor) {
        String v = valor == null ? "" : valor.trim();
        if (v.isEmpty()) {
            erros.add("O telefone é obrigatório.");
        } else if (!SO_NUMEROS.matcher(v).matches() || v.length() > 15) {
            erros.add("O telefone deve ter apenas números (até 15).");
        }
    }

    /** Senha informada (UC01, UC02). */
    public static void senha(List<String> erros, String valor) {
        if (valor == null || valor.isEmpty()) {
            erros.add("A senha é obrigatória.");
        }
    }

    /** Campo de texto obrigatório que não pode ficar só com espaços (descrição do UC10). */
    public static void obrigatorio(List<String> erros, String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            erros.add(campo + " é obrigatória.");
        }
    }
}
