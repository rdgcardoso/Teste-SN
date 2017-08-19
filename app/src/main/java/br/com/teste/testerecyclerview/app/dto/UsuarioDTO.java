package br.com.teste.testerecyclerview.app.dto;

public class UsuarioDTO {
    private long id;
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private UsuarioInformacoesDTO user_info;
    private String key;
    private String django_key;

    public String getDjango_key() {
        return django_key;
    }

    public void setDjango_key(String django_key) {
        this.django_key = django_key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public UsuarioInformacoesDTO getUserInfo() {
        return user_info;
    }

    public void setUserIinfo(UsuarioInformacoesDTO user_info) {
        this.user_info = user_info;
    }

    public String getFoto() {

        return user_info.getFoto();
    }
}

