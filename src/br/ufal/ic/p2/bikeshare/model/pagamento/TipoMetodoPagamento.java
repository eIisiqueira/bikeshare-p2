package br.ufal.ic.p2.bikeshare.model.pagamento;

import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.ValidacaoException;
import br.ufal.ic.p2.bikeshare.model.usuario.Usuario;

/**
 * Fábrica dos métodos de pagamento a partir do token do comando (Factory Method com enum).
 * Cada token sabe quantos dados extras espera e como montar o seu objeto; assim a escolha
 * do tipo acontece uma única vez, aqui, sem {@code instanceof} nem {@code if} espalhado.
 */
public enum TipoMetodoPagamento {
    CARTAO("cartao", 2) {
        @Override
        MetodoPagamento criar(Usuario usuario, String[] dados) {
            return new Cartao(dados[0], dados[1]);
        }
    },
    PIX("pix", 1) {
        @Override
        MetodoPagamento criar(Usuario usuario, String[] dados) {
            return new Pix(dados[0]);
        }
    },
    SALDO_PRE_CARREGADO("saldoPreCarregado", 0) {
        @Override
        MetodoPagamento criar(Usuario usuario, String[] dados) {
            return usuario.getCarteira(); // reaproveita o saldo que o usuário já tinha
        }
    };

    private final String token;
    private final int quantidadeDeDados;

    TipoMetodoPagamento(String token, int quantidadeDeDados) {
        this.token = token;
        this.quantidadeDeDados = quantidadeDeDados;
    }

    abstract MetodoPagamento criar(Usuario usuario, String[] dados);

    /**
     * Cria o método de pagamento indicado pelo token.
     *
     * @param dados dados extras do comando (cartão: número e validade; pix: chave; saldo: nenhum)
     * @throws ValidacaoException "Valor de metodo de pagamento invalido." se o token não existe
     *                            ou veio com a quantidade errada de dados
     */
    public static MetodoPagamento criar(String token, Usuario usuario, String... dados) {
        for (TipoMetodoPagamento tipo : values()) {
            if (tipo.token.equals(token) && tipo.quantidadeDeDados == dados.length) {
                return tipo.criar(usuario, dados);
            }
        }
        throw new ValidacaoException(Mensagens.METODO_PAGAMENTO_INVALIDO);
    }
}
