package br.ufal.ic.p2.bikeshare.model.usuario;

import br.ufal.ic.p2.bikeshare.model.Plano;

import java.math.BigDecimal;

/** Usuário pessoa jurídica, identificado por CNPJ e razão social. Cada aluguel continua individual. */
public class UsuarioPessoaJuridica extends Usuario {
    private static final long serialVersionUID = 1L;
    public static final String TIPO = "pessoaJuridica";

    private final String cnpj;
    private final String razaoSocial;

    public UsuarioPessoaJuridica(String id, String nome, String email, String senha,
                                 Plano plano, BigDecimal mensalidade, String cnpj, String razaoSocial) {
        super(id, nome, email, senha, plano, mensalidade);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getCnpj() {
        return cnpj;
    }

    @Override
    public String getRazaoSocial() {
        return razaoSocial;
    }
}
