package br.ufal.ic.p2.bikeshare.model.pagamento;

import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.util.Validador;

/** Pagamento por Pix: guarda a chave. */
public class Pix extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    private final String chave;

    public Pix(String chave) {
        this.chave = Validador.obrigatorio(chave, Mensagens.CHAVE_PIX_NULA);
    }

    @Override
    public String getNome() {
        return "pix";
    }

    @Override
    public String getChavePix() {
        return chave;
    }
}
