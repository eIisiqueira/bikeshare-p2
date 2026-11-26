package br.ufal.ic.p2.bikeshare.model.pagamento;

import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.RegraDeNegocioException;

/** Usuário ainda sem forma de pagamento (Null Object): evita testes de {@code null} no resto do código. */
public class SemMetodoPagamento extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    @Override
    public String getNome() {
        return "nenhum";
    }

    @Override
    public void validarRetirada() {
        throw new RegraDeNegocioException(Mensagens.METODO_PAGAMENTO_NAO_CADASTRADO);
    }
}
