package br.com.teste.testerecyclerview.app.dto;

public class UsuarioDTO {
    private String id;
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private String password2;
    private String password1;
    private UsuarioInformacoesDTO user_info;

    public UsuarioDTO() {
        user_info = new UsuarioInformacoesDTO();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public UsuarioInformacoesDTO getUserInfo() {
        return user_info;
    }

    public String getFoto() {
        return user_info.getFoto();
    }

    public int getSexo() {
        return user_info.getSexo();
    }

    public String getDataNascimento() {
        return user_info.getDataNascimento();
    }

    public String getDataNascimentoFormatada() {
        return user_info.getDataNascimentoFormatada();
    }

    public String getPassword2() {
        return password2;
    }

    public String getPassword1() {
        return password1;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setUser_info(UsuarioInformacoesDTO user_info) {
        this.user_info = user_info;
    }

    public void setSexo(int sexo) {
        user_info.setSexo(sexo);
    }

    public void setDataNascimento(String data_nascimento) {
        user_info.setDataNascimento(data_nascimento);
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", password2='" + password2 + '\'' +
                ", password1='" + password1 + '\'' +
                ", user_info=" + user_info +
                '}';
    }
}

