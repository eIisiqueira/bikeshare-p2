package br.ufal.ic.p2.bikeshare.exception;

/**
 * Catálogo único das mensagens de erro exigidas pelos testes de aceitação.
 * Centralizar aqui garante que a mesma situação produza sempre o mesmo texto (RNF BS05).
 */
public final class Mensagens {
    private Mensagens() {
    }

    // ---- identificação / existência ----
    public static final String ID_USUARIO_NULO = "Identificacao do usuario nao pode ser nula.";
    public static final String ID_ESTACAO_NULO = "Identificacao da estacao nao pode ser nula.";
    public static final String ID_BICICLETA_NULO = "Identificacao da bicicleta nao pode ser nula.";
    public static final String USUARIO_NAO_EXISTE = "Usuario nao existe.";
    public static final String ESTACAO_NAO_EXISTE = "Estacao nao existe.";
    public static final String BICICLETA_NAO_EXISTE = "Bicicleta nao existe.";
    public static final String ATRIBUTO_NAO_EXISTE = "Atributo nao existe.";
    public static final String ATRIBUTO_NAO_ALTERAVEL = "Atributo nao pode ser alterado.";

    // ---- usuário ----
    public static final String NOME_NULO = "Nome nao pode ser nulo.";
    public static final String EMAIL_NULO = "Email nao pode ser nulo.";
    public static final String EMAIL_INVALIDO = "Email invalido.";
    public static final String EMAIL_JA_CADASTRADO = "Email ja cadastrado.";
    public static final String SENHA_NULA = "Senha nao pode ser nula.";
    public static final String TIPO_INVALIDO = "Tipo invalido.";
    public static final String CPF_NULO = "CPF nao pode ser nulo.";
    public static final String CNPJ_NULO = "CNPJ nao pode ser nulo.";
    public static final String RAZAO_SOCIAL_NULA = "Razao social nao pode ser nula.";
    public static final String NAO_EH_PESSOA_FISICA = "Usuario nao eh pessoa fisica.";
    public static final String NAO_EH_PESSOA_JURIDICA = "Usuario nao eh pessoa juridica.";

    // ---- plano ----
    public static final String PLANO_INVALIDO = "Plano invalido.";
    public static final String PLANO_SEM_MENSALIDADE = "Plano nao possui mensalidade.";
    public static final String MENSALIDADE_NULA = "Mensalidade nao pode ser nula.";
    public static final String MENSALIDADE_NAO_NUMERICA = "Mensalidade deve ser numerica.";
    public static final String MENSALIDADE_NEGATIVA = "Mensalidade deve ser nao-negativa.";

    // ---- método de pagamento ----
    public static final String METODO_PAGAMENTO_INVALIDO = "Valor de metodo de pagamento invalido.";
    public static final String NAO_USA_CARTAO = "Usuario nao usa cartao.";
    public static final String NAO_USA_PIX = "Usuario nao usa pix.";
    public static final String NAO_USA_SALDO = "Usuario nao usa saldo pre-carregado.";
    public static final String NUMERO_CARTAO_NULO = "Numero do cartao nao pode ser nulo.";
    public static final String VALIDADE_CARTAO_NULA = "Validade do cartao nao pode ser nula.";
    public static final String VALIDADE_CARTAO_INVALIDA = "Validade do cartao invalida.";
    public static final String CHAVE_PIX_NULA = "Chave pix nao pode ser nula.";
    public static final String VALOR_NULO = "Valor nao pode ser nulo.";
    public static final String VALOR_NAO_NUMERICO = "Valor deve ser numerico.";
    public static final String VALOR_NAO_POSITIVO = "Valor deve ser positivo.";

    // ---- estação ----
    public static final String ENDERECO_NULO = "Endereco nao pode ser nulo.";
    public static final String CAPACIDADE_NULA = "Capacidade nao pode ser nula.";
    public static final String CAPACIDADE_NAO_NUMERICA = "Capacidade deve ser numerica.";
    public static final String CAPACIDADE_NAO_POSITIVA = "Capacidade deve ser positiva.";
    public static final String ESTACAO_SEM_VAGAS = "Estacao sem vagas disponiveis.";

    // ---- bicicleta ----
    public static final String BICICLETA_JA_EXISTE = "Bicicleta ja existe.";
    public static final String BICICLETA_FORA_DA_ESTACAO = "Bicicleta nao esta nesta estacao.";
    public static final String SEM_BATERIA = "Bicicleta nao possui bateria.";
    public static final String BATERIA_NULA = "Nivel de bateria nao pode ser nulo.";
    public static final String BATERIA_NAO_NUMERICA = "Nivel de bateria deve ser numerico.";
    public static final String BATERIA_FORA_DO_INTERVALO = "Nivel de bateria deve estar entre 0 e 100.";
}
