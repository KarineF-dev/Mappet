package br.com.mappet.util;

import java.util.List;

/** Erro de dados preenchidos pelo usuário. A tela mostra todas as mensagens num alerta. */
public class ValidacaoException extends Exception {
    private final List<String> erros;

    public ValidacaoException(List<String> erros) {
        super(String.join("\n", erros));
        this.erros = erros;
    }

    public ValidacaoException(String erro) {
        this(List.of(erro));
    }

    public List<String> getErros() { return erros; }
}
