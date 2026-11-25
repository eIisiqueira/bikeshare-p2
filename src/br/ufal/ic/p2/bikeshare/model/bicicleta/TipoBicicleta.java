package br.ufal.ic.p2.bikeshare.model.bicicleta;

import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.ValidacaoException;
import br.ufal.ic.p2.bikeshare.util.Validador;

/**
 * Fábrica de bicicletas a partir do token do comando (Factory Method com enum).
 * Único ponto do sistema que "conhece" os tipos concretos.
 */
public enum TipoBicicleta {
    CONVENCIONAL("convencional") {
        @Override
        Bicicleta criar(String id, String nivelBateria) {
            if (nivelBateria != null) {
                throw new ValidacaoException(Mensagens.SEM_BATERIA);
            }
            return new BicicletaConvencional(id);
        }
    },
    ELETRICA("eletrica") {
        @Override
        Bicicleta criar(String id, String nivelBateria) {
            Validador.obrigatorio(nivelBateria, Mensagens.BATERIA_NULA);
            int nivel = Validador.inteiro(nivelBateria, Mensagens.BATERIA_NAO_NUMERICA);
            if (nivel < 0 || nivel > 100) {
                throw new ValidacaoException(Mensagens.BATERIA_FORA_DO_INTERVALO);
            }
            return new BicicletaEletrica(id, nivel);
        }
    };

    private final String token;

    TipoBicicleta(String token) {
        this.token = token;
    }

    abstract Bicicleta criar(String id, String nivelBateria);

    /**
     * Cria a bicicleta do tipo indicado, validando os dados próprios do tipo.
     *
     * @param nivelBateria {@code null} quando o comando não informou bateria
     * @throws ValidacaoException tipo inválido ou bateria inválida/indevida
     */
    public static Bicicleta criar(String token, String id, String nivelBateria) {
        for (TipoBicicleta tipo : values()) {
            if (tipo.token.equals(token)) {
                return tipo.criar(id, nivelBateria);
            }
        }
        throw new ValidacaoException(Mensagens.TIPO_INVALIDO);
    }
}
