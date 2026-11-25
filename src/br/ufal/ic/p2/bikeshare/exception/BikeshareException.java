package br.ufal.ic.p2.bikeshare.exception;

/**
 * Base de todas as exceções de negócio do BIKESHARE.
 * <p>
 * As mensagens vêm sempre de {@link Mensagens}, para que a mesma situação gere a
 * mesma mensagem em qualquer comando (RNF BS05).
 */
public abstract class BikeshareException extends RuntimeException {
    protected BikeshareException(String mensagem) {
        super(mensagem);
    }
}
