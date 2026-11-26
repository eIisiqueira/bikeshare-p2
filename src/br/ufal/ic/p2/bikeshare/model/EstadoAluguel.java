package br.ufal.ic.p2.bikeshare.model;

/** Estados do aluguel (diagrama do PDF). */
public enum EstadoAluguel {
    EM_ANDAMENTO("emAndamento"),
    FINALIZADO("finalizado"),
    CANCELADO("cancelado"),
    EXPIRADO("expirado");

    private final String token;

    EstadoAluguel(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token;
    }
}
