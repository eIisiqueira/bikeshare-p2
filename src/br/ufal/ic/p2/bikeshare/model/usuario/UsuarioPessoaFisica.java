package br.ufal.ic.p2.bikeshare.model.usuario;

import br.ufal.ic.p2.bikeshare.model.Plano;

import java.math.BigDecimal;

/** Usuário pessoa física, identificado por CPF. */
public class UsuarioPessoaFisica extends Usuario {
    private static final long serialVersionUID = 1L;
    public static final String TIPO = "pessoaFisica";

    private final String cpf;

    public UsuarioPessoaFisica(String id, String nome, String email, String senha,
                               Plano plano, BigDecimal mensalidade, String cpf) {
        super(id, nome, email, senha, plano, mensalidade);
        this.cpf = cpf;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getCpf() {
        return cpf;
    }
}
