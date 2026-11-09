package br.com.mappet.controller;

import br.com.mappet.dao.UsuarioDAO;
import br.com.mappet.model.Usuario;
import br.com.mappet.util.Sessao;
import br.com.mappet.util.ValidacaoException;
import br.com.mappet.util.Validador;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/** Regras de cadastro (UC01), login e logout (UC02). */
public class UsuarioController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /** UC01: valida, confere e-mail repetido e grava a senha só como hash. */
    public Usuario cadastrar(String nome, String email, String telefone, String senha)
            throws ValidacaoException, SQLException {
        List<String> erros = new ArrayList<>();
        Validador.textoSemNumeros(erros, nome, "O nome", 100);    // UC01 passo 5
        Validador.email(erros, email);
        Validador.telefone(erros, telefone);
        Validador.senha(erros, senha);
        if (!erros.isEmpty()) throw new ValidacaoException(erros); // UC01 E1

        String emailNormalizado = email.trim().toLowerCase();
        if (usuarioDAO.emailExiste(emailNormalizado)) {             // UC01 E2
            throw new ValidacaoException("Já existe uma conta com esse e-mail. Faça login.");
        }
        String hash = BCrypt.hashpw(senha, BCrypt.gensalt());       // UC01 passo 7
        Usuario u = new Usuario(nome.trim(), emailNormalizado, telefone.trim(), hash);
        usuarioDAO.inserir(u);
        return u;
    }

    /** UC02: confere as credenciais e abre a sessão. */
    public Usuario entrar(String email, String senha) throws ValidacaoException, SQLException {
        List<String> erros = new ArrayList<>();
        Validador.email(erros, email);                             // UC02 passo 4
        Validador.senha(erros, senha);
        if (!erros.isEmpty()) throw new ValidacaoException(erros); // UC02 E1

        Usuario u = usuarioDAO.buscarPorEmail(email.trim().toLowerCase());
        if (u == null || !BCrypt.checkpw(senha, u.getSenhaHash())) {
            // UC02 E2: mesma mensagem para e-mail ou senha errados
            throw new ValidacaoException("E-mail ou senha incorretos.");
        }
        Sessao.entrar(u);
        return u;
    }

    /** UC02 A2: sair da conta. */
    public void sair() {
        Sessao.sair();
    }
}
