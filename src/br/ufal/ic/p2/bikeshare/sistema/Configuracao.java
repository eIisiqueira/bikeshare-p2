package br.ufal.ic.p2.bikeshare.sistema;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Parâmetros de tarifação e limites de negócio (RNF BS04).
 * <p>
 * Todos os números "de negócio" do sistema ficam aqui, num único lugar,
 * em vez de espalhados pelos métodos de cálculo.
 */
public class Configuracao implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal tarifaMinutoAvulso = new BigDecimal("0.50");
    private int franquiaAssinante = 60;
    private BigDecimal excedenteAssinante = new BigDecimal("0.30");
    private BigDecimal mensalidadePadraoAssinante = new BigDecimal("30");
    private BigDecimal adicionalMinutoEletrica = new BigDecimal("0.20");
    private int bateriaMinima = 15;
    private long carenciaMinutos = 2;
    private long tempoMaximoMinutos = 24 * 60;
    private BigDecimal penalidadeAtraso = new BigDecimal("10");

    public BigDecimal getTarifaMinutoAvulso() { return tarifaMinutoAvulso; }
    public int getFranquiaAssinante() { return franquiaAssinante; }
    public BigDecimal getExcedenteAssinante() { return excedenteAssinante; }
    public BigDecimal getMensalidadePadraoAssinante() { return mensalidadePadraoAssinante; }
    public BigDecimal getAdicionalMinutoEletrica() { return adicionalMinutoEletrica; }
    public int getBateriaMinima() { return bateriaMinima; }
    public long getCarenciaMinutos() { return carenciaMinutos; }
    public long getTempoMaximoMinutos() { return tempoMaximoMinutos; }
    public BigDecimal getPenalidadeAtraso() { return penalidadeAtraso; }
}
