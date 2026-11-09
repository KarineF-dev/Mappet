package br.com.mappet.model;

/** Tabela T_MAPPET_ESPECIE: opções válidas de espécie (UC08). */
public class Especie {
    private long id;
    private String nome;

    public Especie(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public long getId() { return id; }
    public String getNome() { return nome; }

    /** Texto mostrado nas listas de seleção. */
    @Override
    public String toString() { return nome; }
}
