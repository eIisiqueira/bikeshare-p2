package br.ufal.ic.p2.bikeshare.model.pagamento;

import br.ufal.ic.p2.bikeshare.exception.AtributoInvalidoException;
import br.ufal.ic.p2.bikeshare.exception.Mensagens;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Forma de pagamento do usuário.
 * <p>
 * A classe base dá o comportamento "não se aplica" para os dados de cada subtipo
 * (número do cartão, chave pix, saldo). Cada subclasse sobrescreve só o que é dela,
 * e o sistema nunca precisa perguntar "que tipo de pagamento é esse?".
 */
public abstract class MetodoPagamento implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Token exibido em {@code getAtributoUsuario ... atributo=metodoPagamento}. */
    public abstract String getNome();

    public String getNumeroCartao() {
        throw new AtributoInvalidoException(Mensagens.NAO_USA_CARTAO);
    }

    public String getValidadeCartao() {
        throw new AtributoInvalidoException(Mensagens.NAO_USA_CARTAO);
    }

    public String getChavePix() {
        throw new AtributoInvalidoException(Mensagens.NAO_USA_PIX);
    }

    public BigDecimal getSaldo() {
        throw new AtributoInvalidoException(Mensagens.NAO_USA_SALDO);
    }
}
