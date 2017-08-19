package br.com.teste.testerecyclerview.app.dto;

import java.util.List;

public class TokenDTO {
    private String key;
    private String detail;
    private List<String> non_field_errors;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<String> getNon_field_errors() {
        return non_field_errors;
    }

    public void setNon_field_errors(List<String> non_field_errors) {
        this.non_field_errors = non_field_errors;
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "key='" + key + '\'' +
                ", detail='" + detail + '\'' +
                ", non_field_errors=" + non_field_errors +
                '}';
    }


}
