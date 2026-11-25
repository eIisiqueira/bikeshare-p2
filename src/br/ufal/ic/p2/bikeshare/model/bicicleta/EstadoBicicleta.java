package br.ufal.ic.p2.bikeshare.model.bicicleta;

/** Estados da bicicleta (diagrama do PDF). Uma bicicleta removida sai do sistema. */
public enum EstadoBicicleta {
    DISPONIVEL("disponivel"),
    EM_USO("emUso"),
    EM_MANUTENCAO("emManutencao");

    private final String token;

    EstadoBicicleta(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token;
    }
}
