package br.ufal.ic.p2.bikeshare.sistema;

import java.io.Serializable;

/**
 * Coordenador do BIKESHARE: guarda e localiza as entidades, gera os identificadores e
 * orquestra as operações, delegando cada regra ao objeto que tem os dados
 * (estação, bicicleta, usuário, aluguel, plano).
 * <p>
 * É o objeto inteiro que é salvo em disco ({@link Persistencia}), incluindo os contadores
 * de id, para que nenhum id seja repetido depois de reiniciar.
 */
public class Sistema implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Configuracao config = new Configuracao();
}
