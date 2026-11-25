package br.ufal.ic.p2.bikeshare.exception;

/** Entrada inválida (campo vazio, formato ou número inválido). */
public class ValidacaoException extends BikeshareException {
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
