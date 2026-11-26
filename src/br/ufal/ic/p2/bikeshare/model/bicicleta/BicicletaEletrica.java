package br.ufal.ic.p2.bikeshare.model.bicicleta;

import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.RegraDeNegocioException;
import br.ufal.ic.p2.bikeshare.sistema.Configuracao;

import java.math.BigDecimal;

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
    public BigDecimal adicionalPorMinuto(Configuracao config) {
        return config.getAdicionalMinutoEletrica();
    }

    @Override
    public boolean estaDisponivel(Configuracao config) {
        return super.estaDisponivel(config) && temBateriaSuficiente(config);
    }

    @Override
    public void validarRetirada(Configuracao config) {
        super.validarRetirada(config);
        if (!temBateriaSuficiente(config)) {
            throw new RegraDeNegocioException(Mensagens.BATERIA_INSUFICIENTE);
        }
    }

    private boolean temBateriaSuficiente(Configuracao config) {
        return nivelBateria >= config.getBateriaMinima();
    }
}
