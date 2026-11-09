package br.com.mappet.model;

/** Tabela T_MAPPET_USUARIO. Tutor, Acolhedor e Adotante são papéis de um mesmo usuário. */
public class Usuario {
    private long id;
    private String nome;
    private String email;
    private String telefone;
    private String senhaHash;

    public Usuario() { }

    public Usuario(String nome, String email, String telefone, String senhaHash) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senhaHash = senhaHash;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }
}
