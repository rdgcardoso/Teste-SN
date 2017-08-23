package br.com.teste.testerecyclerview.domain.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private long id;
    private String username;
    private String email;
    private String nome;
    private String sobrenome;
    private String dataNascimento;
    private String genero;
    private String foto;
    private String senha;
    private String senhaConfirmacao;

    public Usuario(String username, String nome, String sobrenome, String email, String dataNascimento, String genero, String senha, String senhaConfirmacao) {
        this.username = username;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.senha = senha;
        this.senhaConfirmacao = senhaConfirmacao;
    }

    public Usuario(long id, String username, String email, String nome, String sobrenome, String foto) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.foto = foto;
    }

    public Usuario() {
    }

    public String getEmail() {
        return email;
    }

    public String getNomeCompleto() {
        return nome + " " + sobrenome;
    }

    public String getFoto() {
        return foto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenhaConfirmacao() {
        return senhaConfirmacao;
    }

    public void setSenhaConfirmacao(String senhaConfirmacao) {
        this.senhaConfirmacao = senhaConfirmacao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", genero='" + genero + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }

    public void validarUsername() throws Exception {
        if (username == null) {
            throw new Exception("Username não pode ser nulo!");
        } else if (username.isEmpty()) {
            throw new Exception("Campo obrigatório");
        }
    }

    public void validarNome() throws Exception {
        if (nome == null) {
            throw new Exception("Nome não pode ser nulo!");
        } else if (nome.isEmpty()) {
            throw new Exception("Campo obrigatório");
        }
    }

    public void validarSobrenome() throws Exception {
        if (sobrenome == null) {
            throw new Exception("Sobrenome não pode ser nulo!");
        } else if (sobrenome.isEmpty()) {
            throw new Exception("Campo obrigatório");
        }
    }

    public void validarEmail() throws Exception {
        if (email == null) {
            throw new Exception("E-mail não pode ser nulo!");
        } else if (email.isEmpty()) {
            throw new Exception("Campo obrigatório");
        }
    }

    public void validarDataNascimento() throws Exception {
        if (dataNascimento == null) {
            throw new Exception("Data de Nascimento não pode ser nula!");
        } else if (dataNascimento.isEmpty()) {
            throw new Exception("Campo obrigatório");
        }
    }

    public void validarGenero() throws Exception {
        if (genero == null) {
            throw new Exception("Gênero não pode ser nulo!");
        } else if (genero.isEmpty()) {
            throw new Exception("Campo obrigatório");
        }
    }

    public void validarSenha() throws Exception {
        if (senha == null) {
            throw new Exception("Senha não pode ser nula!");
        } else if (senha.isEmpty()) {
            throw new Exception("Campo obrigatório");
        } else if (!senha.equals(senhaConfirmacao)) {
            throw new Exception("Os dois campos de senha não combinam");
        } else if (senha.length()<6) {
            throw new Exception("Esta senha é muito curta. Ela deve ter pelo menos 6 caracteres");
        }
    }

    public void validarSenhaConfirmacao() throws Exception {
        if (senhaConfirmacao == null) {
            throw new Exception("Confirmação de Senha não pode ser nula!");
        } else if (senhaConfirmacao.isEmpty()) {
            throw new Exception("Campo obrigatório");
        } else if (!senhaConfirmacao.equals(senha)) {
            throw new Exception("Os dois campos de senha não combinam");
        } else if (senhaConfirmacao.length()<6) {
            throw new Exception("Esta senha é muito curta. Ela deve ter pelo menos 6 caracteres");
        }
    }
}
