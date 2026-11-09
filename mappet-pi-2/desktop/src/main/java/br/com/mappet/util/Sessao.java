package br.com.mappet.util;

import br.com.mappet.model.Usuario;

/** Guarda quem está logado enquanto o programa está aberto (UC02). */
public class Sessao {
    private static Usuario usuarioLogado;

    public static void entrar(Usuario usuario) { usuarioLogado = usuario; }

    /** UC02 A2: sair da conta. */
    public static void sair() { usuarioLogado = null; }

    public static boolean estaLogado() { return usuarioLogado != null; }

    public static Usuario getUsuario() { return usuarioLogado; }
}
