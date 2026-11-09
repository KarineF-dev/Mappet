package br.com.mappet.model;

import java.time.LocalDateTime;

/** Tabela T_MAPPET_ANIMAL_ADOCAO: animal em lar temporário disponível para adoção (UC10). */
public class AnimalAdocao {
    private long id;
    private String nome;
    private String bairro;
    private String descricao;
    private String fotoUrl;
    private LocalDateTime dataCadastro;
    private long idUsuario;
    private long idEspecie;
    private String nomeEspecie; // vem do JOIN, só para exibir no card

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }
    public long getIdEspecie() { return idEspecie; }
    public void setIdEspecie(long idEspecie) { this.idEspecie = idEspecie; }
    public String getNomeEspecie() { return nomeEspecie; }
    public void setNomeEspecie(String nomeEspecie) { this.nomeEspecie = nomeEspecie; }
}
