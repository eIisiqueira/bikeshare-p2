package br.ufal.ic.p2.bikeshare.sistema;

import br.ufal.ic.p2.bikeshare.exception.EntidadeNaoEncontradaException;
import br.ufal.ic.p2.bikeshare.exception.Mensagens;
import br.ufal.ic.p2.bikeshare.exception.RegraDeNegocioException;
import br.ufal.ic.p2.bikeshare.exception.ValidacaoException;
import br.ufal.ic.p2.bikeshare.model.Plano;
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
    private final Map<String, Plano> planos = new LinkedHashMap<>();
    private int proximoUsuario = 1;

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

    // ====================================================================== buscas

    private Usuario buscarUsuario(String id) {
        return buscar(usuarios, id, Mensagens.ID_USUARIO_NULO, Mensagens.USUARIO_NAO_EXISTE);
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
