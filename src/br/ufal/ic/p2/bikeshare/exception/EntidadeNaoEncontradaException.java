package br.ufal.ic.p2.bikeshare.exception;

/** Usuário, estação, bicicleta, aluguel ou plano inexistente. */
public class EntidadeNaoEncontradaException extends BikeshareException {
    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
