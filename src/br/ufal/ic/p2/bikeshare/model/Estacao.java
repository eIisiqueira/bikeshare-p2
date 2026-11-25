package br.ufal.ic.p2.bikeshare.model;

import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.RegraDeNegocioException;
import br.ufal.ic.p2.bikeshare.model.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.model.bicicleta.EstadoBicicleta;
import br.ufal.ic.p2.bikeshare.sistema.Configuracao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    /** Bicicletas fisicamente presentes (disponíveis ou em manutenção). */
    private final List<Bicicleta> bicicletas = new ArrayList<>();

    public Estacao(String id, String nome, String endereco, int capacidade) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidade = capacidade;
    }

    public int getVagasLivres() {
        return capacidade - bicicletas.size();
    }

    public boolean temVaga() {
        return getVagasLivres() > 0;
    }

    /** Quantas bicicletas daqui podem ser retiradas agora. */
    public int contarDisponiveis(Configuracao config) {
        int total = 0;
        for (Bicicleta b : bicicletas) {
            if (b.estaDisponivel(config)) {
                total++;
            }
        }
        return total;
    }

    public boolean possui(Bicicleta bicicleta) {
        return bicicletas.contains(bicicleta);
    }

    /** @throws RegraDeNegocioException "Bicicleta nao esta nesta estacao." */
    public void validarPossui(Bicicleta bicicleta) {
        if (!possui(bicicleta)) {
            throw new RegraDeNegocioException(Mensagens.BICICLETA_FORA_DA_ESTACAO);
        }
    }

    /** Cadastro de bicicleta nova. @throws RegraDeNegocioException se não houver vaga */
    public void cadastrar(Bicicleta bicicleta) {
        if (!temVaga()) {
            throw new RegraDeNegocioException(Mensagens.ESTACAO_SEM_VAGAS);
        }
        receber(bicicleta, EstadoBicicleta.DISPONIVEL);
    }

    /** Recebe uma bicicleta já com o estado em que ela vai ficar. Quem chama garante que há vaga. */
    public void receber(Bicicleta bicicleta, EstadoBicicleta estado) {
        bicicletas.add(bicicleta);
        bicicleta.estacionar(this, estado);
    }

    /** Retira a bicicleta da lista da estação (retirada por usuário ou remoção do sistema). */
    public void liberar(Bicicleta bicicleta) {
        bicicletas.remove(bicicleta);
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public int getCapacidade() { return capacidade; }
}
