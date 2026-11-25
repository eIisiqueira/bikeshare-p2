package br.ufal.ic.p2.bikeshare.model.bicicleta;

/** Bicicleta convencional: usa todo o comportamento padrão de {@link Bicicleta}. */
public class BicicletaConvencional extends Bicicleta {
    private static final long serialVersionUID = 1L;

    public BicicletaConvencional(String id) {
        super(id);
    }

    @Override
    public String getTipo() {
        return "convencional";
    }
}
