package br.ufal.ic.p2.bikeshare.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Formatação de saída no padrão esperado pelos testes. */
public final class Formatador {
    private Formatador() {
    }

    /** Valor monetário com duas casas e vírgula decimal, ex.: {@code 120,50}. */
    public static String dinheiro(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString().replace('.', ',');
    }
}
