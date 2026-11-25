package br.ufal.ic.p2.bikeshare.util;

import br.ufal.ic.p2.bikeshare.exception.ValidacaoException;

import java.math.BigDecimal;

/**
 * Validações de entrada reaproveitadas por todo o sistema.
 * Cada método recebe a mensagem a lançar, para que quem chama escolha o texto certo.
 */
public final class Validador {
    private Validador() {
    }

    /** Verdadeiro se o texto é nulo, vazio ou só tem espaços. */
    public static boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    /** Lança {@link ValidacaoException} com a mensagem dada se o texto estiver vazio; senão devolve o próprio texto. */
    public static String obrigatorio(String valor, String mensagemSeVazio) {
        if (vazio(valor)) {
            throw new ValidacaoException(mensagemSeVazio);
        }
        return valor;
    }

    /** Converte para inteiro (só dígitos, sem decimais), lançando a mensagem dada se não for possível. */
    public static int inteiro(String valor, String mensagemSeInvalido) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            throw new ValidacaoException(mensagemSeInvalido);
        }
    }

    /** Converte um valor decimal no formato brasileiro ("25,5") ou com ponto, lançando a mensagem dada se inválido. */
    public static BigDecimal decimal(String valor, String mensagemSeInvalido) {
        try {
            return new BigDecimal(valor.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new ValidacaoException(mensagemSeInvalido);
        }
    }
}
