package br.com.teste.testerecyclerview.app.dto;

public class LogoutDTO {

    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "LogoutDTO{" +
                "success='" + success + '\'' +
                '}';
    }
}
