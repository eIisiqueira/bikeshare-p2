package br.ufal.ic.p2.bikeshare.sistema;

import br.ufal.ic.p2.bikeshare.exception.AtributoInvalidoException;
import br.ufal.ic.p2.bikeshare.exception.EntidadeNaoEncontradaException;
import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.RegraDeNegocioException;
import br.ufal.ic.p2.bikeshare.exception.ValidacaoException;
import br.ufal.ic.p2.bikeshare.model.Estacao;
import br.ufal.ic.p2.bikeshare.model.Plano;
import br.ufal.ic.p2.bikeshare.model.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.model.bicicleta.TipoBicicleta;
import br.ufal.ic.p2.bikeshare.model.pagamento.TipoMetodoPagamento;
import br.ufal.ic.p2.bikeshare.model.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.model.usuario.UsuarioPessoaFisica;
import br.ufal.ic.p2.bikeshare.model.usuario.UsuarioPessoaJuridica;
import br.ufal.ic.p2.bikeshare.util.Validador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

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
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();
    private final Map<String, Estacao> estacoes = new LinkedHashMap<>();
    private final Map<String, Bicicleta> bicicletas = new LinkedHashMap<>();
    private final Map<String, Plano> planos = new LinkedHashMap<>();
    private int proximoUsuario = 1;
    private int proximaEstacao = 1;

    public Sistema() {
        registrarPlano(new Plano("avulso", 0, config.getTarifaMinutoAvulso(), null));
        registrarPlano(new Plano("assinante", config.getFranquiaAssinante(),
                config.getExcedenteAssinante(), config.getMensalidadePadraoAssinante()));
    }

    private void registrarPlano(Plano plano) {
        planos.put(plano.getNome(), plano);
    }

    // ====================================================================== US1 - cadastro de usuário

    /**
     * Cadastra um usuário pessoa física.
     *
     * @param plano       {@code null} quando o comando não informa plano (fica avulso)
     * @param mensalidade mensalidade contratada; só faz sentido se {@code plano} for informado
     * @return o id gerado (U1, U2, ...)
     */
    public String criarUsuarioPessoaFisica(String nome, String email, String senha, String tipo, String cpf,
                                           String plano, String mensalidade) {
        validarDadosComuns(nome, email, senha);
        validarTipo(tipo, UsuarioPessoaFisica.TIPO);
        Validador.obrigatorio(cpf, Mensagens.CPF_NULO);
        Plano p = planoInformado(plano);
        BigDecimal m = mensalidadeInformada(p, plano, mensalidade);
        String id = "U" + proximoUsuario++;
        usuarios.put(id, new UsuarioPessoaFisica(id, nome, email, senha, p, m, cpf));
        return id;
    }

    /** Cadastra um usuário pessoa jurídica. Mesmas regras da pessoa física, com CNPJ e razão social. */
    public String criarUsuarioPessoaJuridica(String nome, String email, String senha, String tipo, String cnpj,
                                             String razaoSocial, String plano, String mensalidade) {
        validarDadosComuns(nome, email, senha);
        validarTipo(tipo, UsuarioPessoaJuridica.TIPO);
        Validador.obrigatorio(cnpj, Mensagens.CNPJ_NULO);
        Validador.obrigatorio(razaoSocial, Mensagens.RAZAO_SOCIAL_NULA);
        Plano p = planoInformado(plano);
        BigDecimal m = mensalidadeInformada(p, plano, mensalidade);
        String id = "U" + proximoUsuario++;
        usuarios.put(id, new UsuarioPessoaJuridica(id, nome, email, senha, p, m, cnpj, razaoSocial));
        return id;
    }

    private void validarDadosComuns(String nome, String email, String senha) {
        Validador.obrigatorio(nome, Mensagens.NOME_NULO);
        validarEmailLivre(email, null);
        Validador.obrigatorio(senha, Mensagens.SENHA_NULA);
    }

    private void validarTipo(String tipoInformado, String tipoEsperado) {
        if (!tipoEsperado.equals(tipoInformado)) {
            throw new ValidacaoException(Mensagens.TIPO_INVALIDO);
        }
    }

    private Plano planoInformado(String plano) {
        return plano == null ? planos.get("avulso") : buscarPlano(plano);
    }

    private BigDecimal mensalidadeInformada(Plano p, String plano, String mensalidade) {
        return plano == null ? null : p.lerMensalidadeContratada(mensalidade);
    }

    /**
     * Valida um email novo: obrigatório, com formato válido e não usado por outro usuário
     * (sem diferenciar maiúsculas de minúsculas).
     *
     * @param dono usuário que já é dono do email (pode mantê-lo); {@code null} num cadastro novo
     */
    private void validarEmailLivre(String email, Usuario dono) {
        Validador.obrigatorio(email, Mensagens.EMAIL_NULO);
        if (!email.trim().matches("[^@\\s]+@[^@\\s]+")) {
            throw new ValidacaoException(Mensagens.EMAIL_INVALIDO);
        }
        Usuario atual = procurarPorEmail(email);
        if (atual != null && atual != dono) {
            throw new RegraDeNegocioException(Mensagens.EMAIL_JA_CADASTRADO);
        }
    }

    private Usuario procurarPorEmail(String email) {
        for (Usuario u : usuarios.values()) {
            if (u.getEmail().equalsIgnoreCase(email.trim())) {
                return u;
            }
        }
        return null;
    }

    private Plano buscarPlano(String nome) {
        Plano plano = Validador.vazio(nome) ? null : planos.get(nome);
        if (plano == null) {
            throw new ValidacaoException(Mensagens.PLANO_INVALIDO);
        }
        return plano;
    }

    // ====================================================================== US2 - consulta

    public String getAtributoUsuario(String idUsuario, String atributo) {
        return buscarUsuario(idUsuario).getAtributo(atributo);
    }

    public String getUsuarioPorEmail(String email) {
        Validador.obrigatorio(email, Mensagens.EMAIL_NULO);
        Usuario u = procurarPorEmail(email);
        if (u == null) {
            throw new EntidadeNaoEncontradaException(Mensagens.USUARIO_NAO_EXISTE);
        }
        return u.getId();
    }

    // ====================================================================== US3 - alteração

    /**
     * Altera um atributo simples (nome, email, senha, plano), recarrega saldo ({@code saldo})
     * ou escolhe saldo pré-carregado ({@code metodoPagamento} sem dados extras).
     * Cada ramo valida antes de alterar: uma alteração recusada não muda nada.
     */
    public void alterarUsuario(String idUsuario, String atributo, String valor) {
        Usuario u = buscarUsuario(idUsuario);
        switch (atributo) {
            case "nome" -> u.setNome(Validador.obrigatorio(valor, Mensagens.NOME_NULO));
            case "email" -> {
                validarEmailLivre(valor, u);
                u.setEmail(valor);
            }
            case "senha" -> u.setSenha(Validador.obrigatorio(valor, Mensagens.SENHA_NULA));
            case "plano" -> u.alterarPlano(buscarPlano(valor));
            case "saldo" -> u.getMetodoPagamento().recarregar(valor);
            case "metodoPagamento" -> alterarMetodoPagamento(u, valor);
            default -> throw new AtributoInvalidoException(Mensagens.ATRIBUTO_NAO_ALTERAVEL);
        }
    }

    /**
     * Altera o método de pagamento com dados extras (pix: chave; cartão: número e validade).
     * Só o atributo {@code metodoPagamento} aceita esse formato de comando.
     */
    public void alterarUsuario(String idUsuario, String atributo, String metodo, String... dados) {
        Usuario u = buscarUsuario(idUsuario);
        if (!"metodoPagamento".equals(atributo)) {
            throw new AtributoInvalidoException(Mensagens.ATRIBUTO_NAO_ALTERAVEL);
        }
        alterarMetodoPagamento(u, metodo, dados);
    }

    private void alterarMetodoPagamento(Usuario u, String metodo, String... dados) {
        u.setMetodoPagamento(TipoMetodoPagamento.criar(metodo, u, dados));
    }

    // ====================================================================== US4 - remoção

    public void removerUsuario(String idUsuario) {
        Usuario u = buscarUsuario(idUsuario);
        usuarios.remove(u.getId());
    }

    // ====================================================================== US5 - estação

    /** Cadastra uma estação. Validação na ordem: nome, endereço, capacidade (inteiro positivo). */
    public String criarEstacao(String nome, String endereco, String capacidade) {
        Validador.obrigatorio(nome, Mensagens.NOME_NULO);
        Validador.obrigatorio(endereco, Mensagens.ENDERECO_NULO);
        Validador.obrigatorio(capacidade, Mensagens.CAPACIDADE_NULA);
        int vagas = Validador.inteiro(capacidade, Mensagens.CAPACIDADE_NAO_NUMERICA);
        if (vagas <= 0) {
            throw new ValidacaoException(Mensagens.CAPACIDADE_NAO_POSITIVA);
        }
        String id = "E" + proximaEstacao++;
        estacoes.put(id, new Estacao(id, nome, endereco, vagas));
        return id;
    }

    // ====================================================================== US6 / US7 - bicicletas

    /**
     * Cadastra uma bicicleta numa estação. Os dados da bicicleta são validados antes da vaga.
     *
     * @param nivelBateria {@code null} quando o comando não informa bateria
     */
    public void adicionarBicicleta(String idEstacao, String idBicicleta, String tipo, String nivelBateria) {
        Estacao estacao = buscarEstacao(idEstacao);
        Validador.obrigatorio(idBicicleta, Mensagens.ID_BICICLETA_NULO);
        if (bicicletas.containsKey(idBicicleta)) {
            throw new RegraDeNegocioException(Mensagens.BICICLETA_JA_EXISTE);
        }
        Bicicleta bicicleta = TipoBicicleta.criar(tipo, idBicicleta, nivelBateria);
        estacao.cadastrar(bicicleta);
        bicicletas.put(idBicicleta, bicicleta);
    }

    /** Remove a bicicleta do sistema; o mesmo id pode ser cadastrado de novo depois. */
    public void removerBicicleta(String idEstacao, String idBicicleta) {
        Estacao estacao = buscarEstacao(idEstacao);
        Bicicleta bicicleta = buscarBicicleta(idBicicleta);
        estacao.validarPossui(bicicleta);
        estacao.liberar(bicicleta);
        bicicletas.remove(bicicleta.getId());
    }

    public String getAtributoBicicleta(String idBicicleta, String atributo) {
        return buscarBicicleta(idBicicleta).getAtributo(atributo);
    }

    public String getEstadoBicicleta(String idBicicleta) {
        return buscarBicicleta(idBicicleta).getEstado().toString();
    }

    public int getBicicletasDisponiveis(String idEstacao) {
        return buscarEstacao(idEstacao).contarDisponiveis(config);
    }

    public int getVagasDisponiveis(String idEstacao) {
        return buscarEstacao(idEstacao).getVagasLivres();
    }

    // ====================================================================== buscas

    private Usuario buscarUsuario(String id) {
        return buscar(usuarios, id, Mensagens.ID_USUARIO_NULO, Mensagens.USUARIO_NAO_EXISTE);
    }

    private Estacao buscarEstacao(String id) {
        return buscar(estacoes, id, Mensagens.ID_ESTACAO_NULO, Mensagens.ESTACAO_NAO_EXISTE);
    }

    private Bicicleta buscarBicicleta(String id) {
        return buscar(bicicletas, id, Mensagens.ID_BICICLETA_NULO, Mensagens.BICICLETA_NAO_EXISTE);
    }

    private static <T> T buscar(Map<String, T> mapa, String id, String mensagemIdNulo, String mensagemNaoExiste) {
        Validador.obrigatorio(id, mensagemIdNulo);
        T encontrado = mapa.get(id);
        if (encontrado == null) {
            throw new EntidadeNaoEncontradaException(mensagemNaoExiste);
        }
        return encontrado;
    }
}
