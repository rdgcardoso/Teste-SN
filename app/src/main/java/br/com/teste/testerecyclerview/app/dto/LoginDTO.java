package br.com.teste.testerecyclerview.app.dto;

import java.util.Arrays;

public class LoginDTO {
    private String key;
    private String username;
    private String[] non_field_errors;

    public String getKey() {
        return key;
    }

    public String[] getNon_field_errors() {
        return non_field_errors;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNon_field_errors(String[] non_field_errors) {
        this.non_field_errors = non_field_errors;
    }
}
