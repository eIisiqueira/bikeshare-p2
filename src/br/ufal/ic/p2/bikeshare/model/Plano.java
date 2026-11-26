package br.ufal.ic.p2.bikeshare.model;

import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.ValidacaoException;
import br.ufal.ic.p2.bikeshare.util.Validador;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Modalidade de tarifação: franquia de minutos grátis por corrida + valor por minuto excedente,
 * com ou sem mensalidade.
 * <p>
 * Todos os planos seguem a mesma fórmula, então cada plano é um <b>objeto configurado</b>,
 * não uma subclasse. O avulso é simplesmente "franquia 0, R$0,50 por minuto, sem mensalidade";
 * o assinante é "franquia 60, R$0,30 por minuto excedente, mensalidade padrão R$30".
 * Planos criados em tempo de execução (US12) entram como novos objetos, sem código novo.
 */
public class Plano implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String nome;
    private final int franquiaMinutos;
    private final BigDecimal valorMinutoExcedente;
    /** {@code null} quando o plano não cobra mensalidade (ex.: avulso). */
    private final BigDecimal mensalidadePadrao;

    public Plano(String nome, int franquiaMinutos, BigDecimal valorMinutoExcedente, BigDecimal mensalidadePadrao) {
        this.nome = nome;
        this.franquiaMinutos = franquiaMinutos;
        this.valorMinutoExcedente = valorMinutoExcedente;
        this.mensalidadePadrao = mensalidadePadrao;
    }

    public String getNome() {
        return nome;
    }

    public boolean possuiMensalidade() {
        return mensalidadePadrao != null;
    }

    public BigDecimal getMensalidadePadrao() {
        return mensalidadePadrao;
    }

    /**
     * Calcula o valor de uma corrida.
     *
     * @param minutos            duração da corrida
     * @param adicionalPorMinuto adicional do tipo de bicicleta (zero para convencional);
     *                           é cobrado em todos os minutos, mesmo dentro da franquia
     * @return (minutos além da franquia × valor excedente) + (minutos × adicional)
     */
    public BigDecimal calcularValor(long minutos, BigDecimal adicionalPorMinuto) {
        long excedentes = Math.max(0, minutos - franquiaMinutos);
        BigDecimal tarifa = valorMinutoExcedente.multiply(BigDecimal.valueOf(excedentes));
        BigDecimal adicional = adicionalPorMinuto.multiply(BigDecimal.valueOf(minutos));
        return tarifa.add(adicional);
    }

    /**
     * Interpreta a mensalidade informada ao contratar este plano.
     *
     * @return a mensalidade contratada, ou {@code null} se o plano não tem mensalidade
     * @throws ValidacaoException se o plano não tem mensalidade e uma foi informada,
     *                            ou se tem e o valor está vazio, não é número ou é negativo
     */
    public BigDecimal lerMensalidadeContratada(String texto) {
        if (!possuiMensalidade()) {
            if (!Validador.vazio(texto)) {
                throw new ValidacaoException(Mensagens.PLANO_SEM_MENSALIDADE);
            }
            return null;
        }
        Validador.obrigatorio(texto, Mensagens.MENSALIDADE_NULA);
        BigDecimal valor = Validador.decimal(texto, Mensagens.MENSALIDADE_NAO_NUMERICA);
        if (valor.signum() < 0) {
            throw new ValidacaoException(Mensagens.MENSALIDADE_NEGATIVA);
        }
        return valor;
    }
}
