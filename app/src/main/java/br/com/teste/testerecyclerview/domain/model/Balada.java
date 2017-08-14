package br.com.teste.testerecyclerview.domain.model;

public class Balada {

    private String id;
    //private String[] tipo_musicas;
    private String nome;
    private String descricao;
    private String cep;
    private String estado;
    private String cidade;
    private String bairro;
    private String logradouro;
    private String numero;
    private String complemento;
    private String avaliacao;
    private String site;
    private double preco_medio;
    private boolean ativo;

    public Balada(String id, String nome, String descricao, String cep, String estado, String cidade, String bairro, String logradouro, String numero, String complemento, String avaliacao, String site, double preco_medio, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.cep = cep;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.avaliacao = avaliacao;
        this.site = site;
        this.preco_medio = preco_medio;
        this.ativo = ativo;
    }
}
