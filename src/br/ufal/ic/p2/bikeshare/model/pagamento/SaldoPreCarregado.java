package br.ufal.ic.p2.bikeshare.model.pagamento;

import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.RegraDeNegocioException;
import br.ufal.ic.p2.bikeshare.exception.ValidacaoException;
import br.ufal.ic.p2.bikeshare.util.Validador;

import java.math.BigDecimal;

/**
 * Carteira de saldo pré-carregado. O usuário recarrega; cada corrida é debitada na devolução.
 * Sem saldo positivo não é possível retirar, e uma devolução maior que o saldo é recusada.
 */
public class SaldoPreCarregado extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    private BigDecimal saldo = BigDecimal.ZERO;

    @Override
    public String getNome() {
        return "saldoPreCarregado";
    }

    @Override
    public BigDecimal getSaldo() {
        return saldo;
    }

    @Override
    public void recarregar(String texto) {
        Validador.obrigatorio(texto, Mensagens.VALOR_NULO);
        BigDecimal valor = Validador.decimal(texto, Mensagens.VALOR_NAO_NUMERICO);
        if (valor.signum() <= 0) {
            throw new ValidacaoException(Mensagens.VALOR_NAO_POSITIVO);
        }
        saldo = saldo.add(valor);
    }

    @Override
    public void validarRetirada() {
        if (saldo.signum() <= 0) {
            throw new RegraDeNegocioException(Mensagens.SALDO_INSUFICIENTE);
        }
    }

    @Override
    public void validarCobranca(BigDecimal valor) {
        if (saldo.compareTo(valor) < 0) {
            throw new RegraDeNegocioException(Mensagens.SALDO_INSUFICIENTE);
        }
    }

    @Override
    public void cobrar(BigDecimal valor) {
        validarCobranca(valor);
        saldo = saldo.subtract(valor);
    }
}
