package br.ufal.ic.p2.bikeshare.model.pagamento;

/** Usuário ainda sem forma de pagamento (Null Object): evita testes de {@code null} no resto do código. */
public class SemMetodoPagamento extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    @Override
    public String getNome() {
        return "nenhum";
    }
}
