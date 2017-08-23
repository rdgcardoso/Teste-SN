package br.com.teste.testerecyclerview.app.dto;

import java.util.Arrays;

public class CadastroUsuarioDTO {
    private String key;
    private String[] username;
    private String[] email;
    private String[] first_name;
    private String[] last_name;
    private String[] data_nascimento;
    private String[] password2;
    private String[] password1;
    private String[] sexo;
    private String[] non_field_errors;

    public String[] getUsername() {
        return username;
    }

    public void setUsername(String[] username) {
        this.username = username;
    }

    public String[] getEmail() {
        return email;
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

    public String[] getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String[] first_name) {
        this.first_name = first_name;
    }

    public String[] getLast_name() {
        return last_name;
    }

    public void setLast_name(String[] last_name) {
        this.last_name = last_name;
    }

    public String[] getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String[] data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String[] getPassword2() {
        return password2;
    }

    public void setPassword2(String[] password2) {
        this.password2 = password2;
    }

    public String[] getPassword1() {
        return password1;
    }

    public void setPassword1(String[] password1) {
        this.password1 = password1;
    }

    public String[] getSexo() {
        return sexo;
    }

    public void setSexo(String[] sexo) {
        this.sexo = sexo;
    }

    public String[] getNon_field_errors() {
        return non_field_errors;
    }

    public void setNon_field_errors(String[] non_field_errors) {
        this.non_field_errors = non_field_errors;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "CadastroUsuarioDTO{" +
                "key='" + key + '\'' +
                ", username=" + Arrays.toString(username) +
                ", email=" + Arrays.toString(email) +
                ", first_name=" + Arrays.toString(first_name) +
                ", last_name=" + Arrays.toString(last_name) +
                ", data_nascimento=" + Arrays.toString(data_nascimento) +
                ", password2=" + Arrays.toString(password2) +
                ", password1=" + Arrays.toString(password1) +
                ", sexo=" + Arrays.toString(sexo) +
                ", non_field_errors=" + Arrays.toString(non_field_errors) +
                '}';
    }
}

