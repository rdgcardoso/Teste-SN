package br.com.teste.testerecyclerview.domain.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

public class Balada implements Serializable{

    private long id;
    private String[] tipo_musicas;
    private String nome;
    private String descricao;
    private String cep;
    private String estado;
    private String cidade;
    private String bairro;
    private String logradouro;
    private String numero;
    private String complemento;
    private float avaliacao;
    private String site;
    private double preco_medio;
    private boolean ativo;
    private String foto;

    public Balada(long id, String[] tipo_musicas, String nome, String descricao, String cep, String estado, String cidade, String bairro, String logradouro, String numero, String complemento, float avaliacao, String site, double preco_medio, boolean ativo, String foto) {
        this.id = id;
        this.tipo_musicas = tipo_musicas;
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
        this.foto = foto;
    }

    public String getTipoMusicas() {

        String tipos = "";

        if (tipo_musicas != null) {
            tipos = tipo_musicas[0];
            for (int i = 1; i < tipo_musicas.length; i++ ) {
                tipos += ", " + tipo_musicas[i];
            }
        }

        return tipos;
    }

    public String getEndereco1() {
        return logradouro + " " + numero + ", " + complemento;
    }

    public String getEndereco2() {
        return bairro + " - " + cidade + " - " + estado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPrecoMedio() {
        DecimalFormat decimalFormat = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        return "R$" + decimalFormat.format(preco_medio);
    }

    public void setPreco_medio(double preco_medio) {
        this.preco_medio = preco_medio;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Balada{" +
                "id=" + id +
                ", tipo_musicas=" + Arrays.toString(tipo_musicas) +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", cep='" + cep + '\'' +
                ", estado='" + estado + '\'' +
                ", cidade='" + cidade + '\'' +
                ", bairro='" + bairro + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", numero='" + numero + '\'' +
                ", complemento='" + complemento + '\'' +
                ", avaliacao=" + avaliacao +
                ", site='" + site + '\'' +
                ", preco_medio=" + preco_medio +
                ", ativo=" + ativo +
                ", foto='" + foto + '\'' +
                '}';
    }
}
