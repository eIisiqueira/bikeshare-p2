package br.ufal.ic.p2.bikeshare.model.pagamento;

import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.ValidacaoException;
import br.ufal.ic.p2.bikeshare.util.Validador;

/** Pagamento por cartão: guarda número e validade (MM/AAAA). */
public class Cartao extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    private final String numero;
    private final String validade;

    public Cartao(String numero, String validade) {
        Validador.obrigatorio(numero, Mensagens.NUMERO_CARTAO_NULO);
        Validador.obrigatorio(validade, Mensagens.VALIDADE_CARTAO_NULA);
        if (!validade.trim().matches("(0?[1-9]|1[0-2])/\\d{4}")) {
            throw new ValidacaoException(Mensagens.VALIDADE_CARTAO_INVALIDA);
        }
        this.numero = numero;
        this.validade = validade;
    }

    @Override
    public String getNome() {
        return "cartao";
    }

    @Override
    public String getNumeroCartao() {
        return numero;
    }

    @Override
    public String getValidadeCartao() {
        return validade;
    }
}
