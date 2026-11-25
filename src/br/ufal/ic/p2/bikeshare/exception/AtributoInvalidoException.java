package br.ufal.ic.p2.bikeshare.exception;

/** Atributo inexistente, não alterável ou que não se aplica ao subtipo do objeto. */
public class AtributoInvalidoException extends BikeshareException {
    public AtributoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
