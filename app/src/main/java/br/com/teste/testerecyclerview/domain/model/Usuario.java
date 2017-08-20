package br.com.teste.testerecyclerview.domain.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private long id;
    private String username;
    private String email;
    private String nome;
    private String sobrenome;
    private String foto;

    public Usuario(long id, String username, String email, String nome, String sobrenome, String foto) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.foto = foto;
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

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
