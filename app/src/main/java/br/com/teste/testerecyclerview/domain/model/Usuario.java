package br.com.teste.testerecyclerview.domain.model;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Usuario implements Serializable {

    private String id;
    private String username;
    private String email;
    private String nome;
    private String sobrenome;
    private String dataNascimentoFormatada;
    private String foto;
    private String senha;
    private String senhaConfirmacao;
    private Genero genero;

    public Usuario() {
        genero = new Genero();
    }

    public Usuario(String username, String senha) {
        this.username = username;
        this.senha = senha;
    }

    //TODO remover construtor
    public Usuario(String id, String username, String email, String nome, String sobrenome, String foto, int genero, String dataNascimentoFormatada) {

        this.genero = new Genero();

        this.id = id;
        this.username = username;
        this.email = email;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.foto = foto;
        this.genero.setId(genero);
        this.dataNascimentoFormatada = dataNascimentoFormatada;
    }

    //TODO corrigir método, data < dia atual
    public int getIdade() {

        Log.d("LRDG", "DATA NASC: " + dataNascimentoFormatada);

        Calendar dataNasc = Calendar.getInstance();
        Calendar hoje = Calendar.getInstance();
        int ano, mes, dia;

        dia = Integer.parseInt(dataNascimentoFormatada.substring(0, 2));
        mes = Integer.parseInt(dataNascimentoFormatada.substring(3, 5));
        ano = Integer.parseInt(dataNascimentoFormatada.substring(6, 10));

        Log.d("LRDG", "Dia "+dia+" |Mês "+mes+" |Ano "+ano);
        dataNasc.set(ano, mes, dia);

        int idade = hoje.get(Calendar.YEAR) - dataNasc.get(Calendar.YEAR);

        Log.d("LRDG", "Idade "+idade);

        if (hoje.get(Calendar.DAY_OF_YEAR) < dataNasc.get(Calendar.DAY_OF_YEAR)){
            idade--;
        }

        return idade;
    }

    public int getGeneroId() {
        return genero.getId();
    }

    public String getGeneroDescricao() {
        return genero.getDescricao();
    }

    public void setGeneroDescricao(String generoDescricao) {
        genero.setDescricao(generoDescricao);
    }

    public void setGeneroId(int generoId) {
        genero.setId(generoId);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getDataNascimentoFormatada() {
        return dataNascimentoFormatada;
    }

    public void setDataNascimentoFormatada(String dataNascimentoFormatada) {
        this.dataNascimentoFormatada = dataNascimentoFormatada;
    }

    public String getDataNascimento() {

        String dataNascimento =
                dataNascimentoFormatada.substring(6, 10) + "-" + dataNascimentoFormatada.substring(3, 5) + "-" + dataNascimentoFormatada.substring(0, 2);

        Log.d("LRDG", "DATA NASCIMENTO getDataNascimento = " + dataNascimento);

        return dataNascimento;
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

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", dataNascimentoFormatada='" + dataNascimentoFormatada + '\'' +
                ", foto='" + foto + '\'' +
                ", senha='" + senha + '\'' +
                ", senhaConfirmacao='" + senhaConfirmacao + '\'' +
                ", genero=" + genero +
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
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new Exception("E-mail inválido");
        }
    }

    public void validarDataNascimento() throws Exception {
        if (dataNascimentoFormatada == null) {
            throw new Exception("Data de Nascimento não pode ser nula!");
        } else if (dataNascimentoFormatada.isEmpty()) {
            throw new Exception("Campo obrigatório");
        }
    }

    public void validarGenero() throws Exception {
        if (genero == null) {
            throw new Exception("Gênero não pode ser nulo!");
        } else if (genero.getId() < 1) {
            throw new Exception("Campo obrigatório");
        }
    }

    public void validarSenha() throws Exception {
        if (senha == null) {
            throw new Exception("Senha não pode ser nula!");
        } else if (senha.isEmpty()) {
            throw new Exception("Campo obrigatório");
        } else if (senha.length() < 6) {
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
        } else if (senhaConfirmacao.length() < 6) {
            throw new Exception("Esta senha é muito curta. Ela deve ter pelo menos 6 caracteres");
        }
    }
}
