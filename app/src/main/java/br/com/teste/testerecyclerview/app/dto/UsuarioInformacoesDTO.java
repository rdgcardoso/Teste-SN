package br.com.teste.testerecyclerview.app.dto;

public class UsuarioInformacoesDTO {
    private String foto;
    private int sexo;
    private String data_nascimento;
    private String data_nascimento_formatada;

    public int getSexo() {
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

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public void setDataNascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    @Override
    public String toString() {
        return "UsuarioInformacoesDTO{" +
                "foto='" + foto + '\'' +
                ", sexo='" + sexo + '\'' +
                ", data_nascimento='" + data_nascimento + '\'' +
                ", data_nascimento_formatada='" + data_nascimento_formatada + '\'' +
                '}';
    }
}
