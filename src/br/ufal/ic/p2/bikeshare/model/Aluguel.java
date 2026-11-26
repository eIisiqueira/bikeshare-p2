package br.ufal.ic.p2.bikeshare.model;

import br.ufal.ic.p2.bikeshare.exception.AtributoInvalidoException;
import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.RegraDeNegocioException;
import br.ufal.ic.p2.bikeshare.model.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.model.bicicleta.EstadoBicicleta;
import br.ufal.ic.p2.bikeshare.model.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.sistema.Configuracao;
import br.ufal.ic.p2.bikeshare.util.Datas;
import br.ufal.ic.p2.bikeshare.util.Formatador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
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
     * Devolve a bicicleta e encerra o aluguel. Todas as validações vêm antes de qualquer alteração.
     * <ul>
     *   <li>passou do tempo máximo → {@code expirado}: sem tarifa, penalidade automática,
     *       usuário inadimplente e bicicleta em manutenção;</li>
     *   <li>mesma estação dentro da carência → {@code cancelado}, sem cobrança;</li>
     *   <li>senão → {@code finalizado}, valor calculado pelo plano do usuário e cobrado no método de pagamento.</li>
     * </ul>
     *
     * @throws RegraDeNegocioException aluguel não está em andamento, data anterior à retirada,
     *                                 estação cheia ou saldo insuficiente (nesses casos nada muda)
     */
    public void devolver(Estacao destino, LocalDateTime dataFim, Configuracao config) {
        if (estado != EstadoAluguel.EM_ANDAMENTO) {
            throw new RegraDeNegocioException(Mensagens.ALUGUEL_NAO_EM_ANDAMENTO);
        }
        if (dataFim.isBefore(inicio)) {
            throw new RegraDeNegocioException(Mensagens.DEVOLUCAO_ANTES_DA_RETIRADA);
        }
        if (!destino.temVaga()) {
            throw new RegraDeNegocioException(Mensagens.ESTACAO_CHEIA);
        }
        long minutos = Duration.between(inicio, dataFim).toMinutes();

        if (minutos > config.getTempoMaximoMinutos()) {
            usuario.aplicarPenalidade(config.getPenalidadeAtraso());
            encerrar(destino, dataFim, EstadoAluguel.EXPIRADO, EstadoBicicleta.EM_MANUTENCAO, BigDecimal.ZERO);
        } else if (destino == estacaoOrigem && minutos <= config.getCarenciaMinutos()) {
            encerrar(destino, dataFim, EstadoAluguel.CANCELADO, EstadoBicicleta.DISPONIVEL, BigDecimal.ZERO);
        } else {
            BigDecimal valor = usuario.getPlano().calcularValor(minutos, bicicleta.adicionalPorMinuto(config));
            usuario.getMetodoPagamento().validarCobranca(valor);
            usuario.getMetodoPagamento().cobrar(valor);
            encerrar(destino, dataFim, EstadoAluguel.FINALIZADO, EstadoBicicleta.DISPONIVEL, valor);
        }
    }

    private void encerrar(Estacao destino, LocalDateTime dataFim, EstadoAluguel estadoFinal,
                          EstadoBicicleta estadoBicicleta, BigDecimal valor) {
        this.fim = dataFim;
        this.estacaoDestino = destino;
        this.valorCobrado = valor;
        this.estado = estadoFinal;
        destino.receber(bicicleta, estadoBicicleta);
        usuario.encerrarAluguel();
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
