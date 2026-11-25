package br.ufal.ic.p2.bikeshare.model;

import br.ufal.ic.p2.bikeshare.sistema.Configuracao;

import java.io.Serializable;

/**
 * Estação com número fixo de vagas. Sabe responder sobre as próprias vagas e bicicletas:
 * quem precisa saber se há vaga pergunta à estação, em vez de fazer a conta fora dela.
 */
public class Estacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String nome;
    private final String endereco;
    private final int capacidade;

    public Estacao(String id, String nome, String endereco, int capacidade) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidade = capacidade;
    }

    /** Ainda sem bicicletas cadastradas (US6): todas as vagas estão livres. */
    public int getVagasLivres() {
        return capacidade;
    }

    /** Ainda sem bicicletas cadastradas (US6): nenhuma disponível. */
    public int contarDisponiveis(Configuracao config) {
        return 0;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public int getCapacidade() { return capacidade; }
}
