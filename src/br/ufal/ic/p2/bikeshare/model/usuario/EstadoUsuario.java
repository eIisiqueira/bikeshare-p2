package br.ufal.ic.p2.bikeshare.model.usuario;

/** Estados do usuário (diagrama do PDF): ativo ou inadimplente. */
public enum EstadoUsuario {
    ATIVO("ativo"),
    INADIMPLENTE("inadimplente");

    private final String token;

    EstadoUsuario(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token;
    }
}
