package br.ufal.ic.p2.bikeshare.model;

import br.ufal.ic.p2.bikeshare.exception.AtributoInvalidoException;
import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.RegraDeNegocioException;
import br.ufal.ic.p2.bikeshare.model.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.model.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.util.Datas;
import br.ufal.ic.p2.bikeshare.util.Formatador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Uma corrida: do momento da retirada até a devolução.
 * O aluguel conhece sua duração e decide como termina (finalizado, cancelado ou expirado).
 */
public class Aluguel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final Usuario usuario;
    private final Bicicleta bicicleta;
    private final Estacao estacaoOrigem;
    private final LocalDateTime inicio;
    private LocalDateTime fim;
    private Estacao estacaoDestino;
    private BigDecimal valorCobrado = BigDecimal.ZERO;
    private EstadoAluguel estado = EstadoAluguel.EM_ANDAMENTO;

    public Aluguel(String id, Usuario usuario, Bicicleta bicicleta, Estacao estacaoOrigem, LocalDateTime inicio) {
        this.id = id;
        this.usuario = usuario;
        this.bicicleta = bicicleta;
        this.estacaoOrigem = estacaoOrigem;
        this.inicio = inicio;
    }

    /**
     * Devolve um atributo formatado. Dados de devolução (fim, destino, valor) só existem
     * depois que o aluguel sai de "em andamento".
     */
    public String getAtributo(String atributo) {
        return switch (atributo) {
            case "estado" -> estado.toString();
            case "usuario" -> usuario.getId();
            case "bicicleta" -> bicicleta.getId();
            case "estacaoOrigem" -> estacaoOrigem.getId();
            case "inicio" -> Datas.formatar(inicio);
            case "fim" -> Datas.formatar(exigirEncerrado().fim);
            case "estacaoDestino" -> exigirEncerrado().estacaoDestino.getId();
            case "valorCobrado" -> Formatador.dinheiro(exigirEncerrado().valorCobrado);
            default -> throw new AtributoInvalidoException(Mensagens.ATRIBUTO_NAO_EXISTE);
        };
    }

    private Aluguel exigirEncerrado() {
        if (estado == EstadoAluguel.EM_ANDAMENTO) {
            throw new RegraDeNegocioException(Mensagens.ALUGUEL_EM_ANDAMENTO);
        }
        return this;
    }

    public String getId() { return id; }
    public EstadoAluguel getEstado() { return estado; }
    public BigDecimal getValorCobrado() { return valorCobrado; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFim() { return fim; }
}
