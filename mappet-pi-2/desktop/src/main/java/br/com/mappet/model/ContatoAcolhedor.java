package br.com.mappet.model;

/** Contato do acolhedor exibido ao adotante depois de registrar o interesse (UC11). */
public class ContatoAcolhedor {
    private final String nome;
    private final String telefone;
    private final String email;
    private final boolean interesseJaRegistrado;

    public ContatoAcolhedor(String nome, String telefone, String email, boolean interesseJaRegistrado) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.interesseJaRegistrado = interesseJaRegistrado;
    }

    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    /** true quando o adotante já tinha manifestado interesse neste animal (UC11 A2). */
    public boolean isInteresseJaRegistrado() { return interesseJaRegistrado; }
}
