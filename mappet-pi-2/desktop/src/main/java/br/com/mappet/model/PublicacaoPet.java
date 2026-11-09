package br.com.mappet.model;

import java.time.LocalDateTime;

/** Tabela T_MAPPET_PUBLICACAO_PET: pet perdido (UC05) ou encontrado (UC06). */
public class PublicacaoPet {
    public static final String PERDIDO = "perdido";
    public static final String ENCONTRADO = "encontrado";

    private long id;
    private String tipo;
    private String nomePet;
    private String bairro;
    private String fotoUrl;
    private LocalDateTime dataPublicacao;
    private long idUsuario;
    private long idEspecie;
    private String nomeEspecie; // vem do JOIN, só para exibir no card

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getNomePet() { return nomePet; }
    public void setNomePet(String nomePet) { this.nomePet = nomePet; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }
    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }
    public long getIdEspecie() { return idEspecie; }
    public void setIdEspecie(long idEspecie) { this.idEspecie = idEspecie; }
    public String getNomeEspecie() { return nomeEspecie; }
    public void setNomeEspecie(String nomeEspecie) { this.nomeEspecie = nomeEspecie; }
}
