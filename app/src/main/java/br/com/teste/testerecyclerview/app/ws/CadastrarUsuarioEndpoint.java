package br.com.teste.testerecyclerview.app.ws;


import br.com.teste.testerecyclerview.app.dto.CadastroUsuarioDTO;
import br.com.teste.testerecyclerview.app.dto.LogoutDTO;
import br.com.teste.testerecyclerview.app.dto.UsuarioDTO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CadastrarUsuarioEndpoint {

    @FormUrlEncoded
    @POST("registration/")
    Call<CadastroUsuarioDTO> cadastrarUsuario(
            @Field("username") String username,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("data_nascimento") String data_nascimento,
            @Field("sexo") String sexo,
            @Field("password1") String password1,
            @Field("password2") String password2
    );

}
