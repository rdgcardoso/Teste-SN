package br.com.teste.testerecyclerview.app.dto;

import java.util.Arrays;

public class UsuarioInfoFormularioDTO {

    private String[] sexo;
    private String[] data_nascimento;

    public String[] getSexo() {
        return sexo;
    }

    public void setSexo(String[] sexo) {
        this.sexo = sexo;
    }

    public String[] getDataNascimento() {
        return data_nascimento;
    }

    public void setDataNascimento(String[] data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    @Override
    public String toString() {
        return "UsuarioInfoFormularioDTO{" +
                ", sexo=" + Arrays.toString(sexo) +
                ", data_nascimento=" + Arrays.toString(data_nascimento) +
                '}';
    }
}

