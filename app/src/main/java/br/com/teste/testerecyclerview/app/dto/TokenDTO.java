package br.com.teste.testerecyclerview.app.dto;

import java.util.List;

public class TokenDTO {
    private String key;
    private String detail;
    //private List<String> non_field_errors;

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "key='" + key + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
