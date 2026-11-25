package br.ufal.ic.p2.bikeshare.model.usuario;

import br.ufal.ic.p2.bikeshare.exception.AtributoInvalidoException;
import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.model.Plano;
import br.ufal.ic.p2.bikeshare.model.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.model.pagamento.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.model.pagamento.SemMetodoPagamento;
import br.ufal.ic.p2.bikeshare.util.Formatador;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Usuário do BIKESHARE. Concentra tudo o que pessoa física e jurídica têm em comum
 * (dados de acesso, plano, pagamento, estado, aluguel ativo, pendências e histórico).
 * <p>
 * O que muda entre os subtipos é só o documento: CPF ({@link UsuarioPessoaFisica}) ou
 * CNPJ + razão social ({@link UsuarioPessoaJuridica}). A base responde "não se aplica"
 * para esses atributos e cada subclasse sobrescreve o seu.
 */
public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String nome;
    private String email;
    private String senha;
    private Plano plano;
    /** Mensalidade contratada; {@code null} se o plano não tem mensalidade. */
    private BigDecimal mensalidade;
    private MetodoPagamento metodoPagamento = new SemMetodoPagamento();
    /** Carteira guardada mesmo quando o usuário troca para outro método, para não perder o saldo. */
    private SaldoPreCarregado carteira;
    private EstadoUsuario estado = EstadoUsuario.ATIVO;
    private BigDecimal pendencias = BigDecimal.ZERO;

    protected Usuario(String id, String nome, String email, String senha, Plano plano, BigDecimal mensalidade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.plano = plano;
        this.mensalidade = mensalidade;
    }

    /** Token do tipo: {@code pessoaFisica} ou {@code pessoaJuridica}. */
    public abstract String getTipo();

    public String getCpf() {
        throw new AtributoInvalidoException(Mensagens.NAO_EH_PESSOA_FISICA);
    }

    public String getCnpj() {
        throw new AtributoInvalidoException(Mensagens.NAO_EH_PESSOA_JURIDICA);
    }

    public String getRazaoSocial() {
        throw new AtributoInvalidoException(Mensagens.NAO_EH_PESSOA_JURIDICA);
    }

    /**
     * Devolve um atributo já formatado como os testes esperam.
     *
     * @throws AtributoInvalidoException se o atributo não existe ou não se aplica a este usuário
     */
    public String getAtributo(String atributo) {
        return switch (atributo) {
            case "nome" -> nome;
            case "email" -> email;
            case "senha" -> senha;
            case "tipo" -> getTipo();
            case "estado" -> estado.toString();
            case "plano" -> plano.getNome();
            case "mensalidade" -> Formatador.dinheiro(getMensalidade());
            case "cpf" -> getCpf();
            case "cnpj" -> getCnpj();
            case "razaoSocial" -> getRazaoSocial();
            case "metodoPagamento" -> metodoPagamento.getNome();
            case "numeroCartao" -> metodoPagamento.getNumeroCartao();
            case "validadeCartao" -> metodoPagamento.getValidadeCartao();
            case "chavePix" -> metodoPagamento.getChavePix();
            case "saldo" -> Formatador.dinheiro(metodoPagamento.getSaldo());
            case "aluguelAtivo" -> "nenhum"; // aluguéis chegam na US8
            case "totalPendencias" -> Formatador.dinheiro(pendencias);
            default -> throw new AtributoInvalidoException(Mensagens.ATRIBUTO_NAO_EXISTE);
        };
    }

    public BigDecimal getMensalidade() {
        if (mensalidade == null) {
            throw new AtributoInvalidoException(Mensagens.PLANO_SEM_MENSALIDADE);
        }
        return mensalidade;
    }

    /**
     * Troca de plano. Escolher o plano atual não muda nada (preserva a mensalidade contratada);
     * outro plano entra com a mensalidade padrão dele.
     */
    public void alterarPlano(Plano novo) {
        if (novo == plano) {
            return;
        }
        plano = novo;
        mensalidade = novo.getMensalidadePadrao();
    }

    /** Carteira de saldo do usuário, criada na primeira vez que é escolhida. */
    public SaldoPreCarregado getCarteira() {
        if (carteira == null) {
            carteira = new SaldoPreCarregado();
        }
        return carteira;
    }

    // ---------------------------------------------------------------- getters/setters simples

    public String getId() { return id; }
    public String getEmail() { return email; }
    public MetodoPagamento getMetodoPagamento() { return metodoPagamento; }

    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) { this.metodoPagamento = metodoPagamento; }
}
