package br.ufal.ic.p2.bikeshare.model.bicicleta;

import br.ufal.ic.p2.bikeshare.exception.AtributoInvalidoException;
import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.model.Estacao;
import br.ufal.ic.p2.bikeshare.sistema.Configuracao;

import java.io.Serializable;

/**
 * Bicicleta compartilhada. A base define o comportamento da bicicleta convencional;
 * {@link BicicletaEletrica} sobrescreve só o que muda (bateria e adicional por minuto).
 * Assim retirada, devolução e contagem de disponíveis tratam qualquer bicicleta do mesmo jeito.
 */
public abstract class Bicicleta implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private EstadoBicicleta estado = EstadoBicicleta.DISPONIVEL;
    /** Estação onde está; {@code null} enquanto está em uso. */
    private Estacao estacao;

    protected Bicicleta(String id) {
        this.id = id;
    }

    /** Token do tipo: {@code convencional} ou {@code eletrica}. */
    public abstract String getTipo();

    public String getNivelBateria() {
        throw new AtributoInvalidoException(Mensagens.SEM_BATERIA);
    }

    /** Se a bicicleta entra na contagem de "disponíveis" da estação. */
    public boolean estaDisponivel(Configuracao config) {
        return estado == EstadoBicicleta.DISPONIVEL;
    }

    public String getAtributo(String atributo) {
        return switch (atributo) {
            case "tipo" -> getTipo();
            case "estado" -> estado.toString();
            case "estacao" -> estacao == null ? "nenhuma" : estacao.getId();
            case "nivelBateria" -> getNivelBateria();
            default -> throw new AtributoInvalidoException(Mensagens.ATRIBUTO_NAO_EXISTE);
        };
    }

    /** Entra numa estação (cadastro ou devolução) já com o novo estado. */
    public void estacionar(Estacao destino, EstadoBicicleta novoEstado) {
        estacao = destino;
        estado = novoEstado;
    }

    public String getId() { return id; }
    public EstadoBicicleta getEstado() { return estado; }
    public Estacao getEstacao() { return estacao; }
}
