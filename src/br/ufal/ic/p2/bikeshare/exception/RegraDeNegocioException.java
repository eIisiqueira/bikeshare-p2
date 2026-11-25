package br.ufal.ic.p2.bikeshare.exception;

/** Operação recusada por uma regra de negócio (estado, vaga, saldo, bateria...). */
public class RegraDeNegocioException extends BikeshareException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
