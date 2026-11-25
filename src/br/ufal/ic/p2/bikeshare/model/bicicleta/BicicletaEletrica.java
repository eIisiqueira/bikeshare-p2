package br.ufal.ic.p2.bikeshare.model.bicicleta;

import br.ufal.ic.p2.bikeshare.sistema.Configuracao;

/**
 * Bicicleta elétrica: tem nível de bateria, cobra adicional por minuto e só pode ser
 * retirada (e só conta como disponível) com bateria no mínimo configurado.
 */
public class BicicletaEletrica extends Bicicleta {
    private static final long serialVersionUID = 1L;

    private final int nivelBateria;

    public BicicletaEletrica(String id, int nivelBateria) {
        super(id);
        this.nivelBateria = nivelBateria;
    }

    @Override
    public String getTipo() {
        return "eletrica";
    }

    @Override
    public String getNivelBateria() {
        return String.valueOf(nivelBateria);
    }

    @Override
    public boolean estaDisponivel(Configuracao config) {
        return super.estaDisponivel(config) && temBateriaSuficiente(config);
    }

    private boolean temBateriaSuficiente(Configuracao config) {
        return nivelBateria >= config.getBateriaMinima();
    }
}
