package br.ufal.ic.p2.bikeshare.util;

import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.ValidacaoException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Ponto único de leitura e escrita de data/hora ("d/M/aaaa H:mm").
 * O sistema nunca usa o relógio real: toda data vem do comando.
 */
public final class Datas {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("d/M/uuuu H:mm").withResolverStyle(ResolverStyle.STRICT);

    private Datas() {
    }

    /**
     * Lê uma data/hora. Aceita zeros à esquerda ("08:00", "29/02/2024").
     *
     * @throws ValidacaoException "Data nao pode ser nula." ou "Data invalida."
     */
    public static LocalDateTime ler(String texto) {
        Validador.obrigatorio(texto, Mensagens.DATA_NULA);
        try {
            return LocalDateTime.parse(texto.trim(), FORMATO);
        } catch (DateTimeParseException e) {
            throw new ValidacaoException(Mensagens.DATA_INVALIDA);
        }
    }

    /** Escreve a data/hora sem zeros à esquerda, ex.: {@code 10/3/2025 8:00}. */
    public static String formatar(LocalDateTime data) {
        return data.format(FORMATO);
    }
}
