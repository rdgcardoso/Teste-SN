package br.com.teste.testerecyclerview.app.dto;

public class UsuarioInformacoesDTO {
    private String foto;
    private String sexo;
    private String data_nascimento;
    private String data_nascimento_formatada;


    public String getSexo() {
        return sexo;
    }

    public String getFoto() {
        return foto;
    }

    public String getDataNascimento() {
        return data_nascimento;
    }

    public String getDataNascimentoFormatada() {
        return data_nascimento_formatada;
    }

}
