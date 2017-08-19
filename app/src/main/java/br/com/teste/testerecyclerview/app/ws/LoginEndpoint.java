package br.com.teste.testerecyclerview.app.ws;


import java.util.List;

import br.com.teste.testerecyclerview.app.dto.TokenDTO;
import br.com.teste.testerecyclerview.app.dto.UsuarioDTO;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginEndpoint {

    @FormUrlEncoded
    @POST("login/")
    Call<TokenDTO> logarUsuario(@Field("username") String username, @Field("password") String password);

    //SharedPreferences ou AccountManager
}
