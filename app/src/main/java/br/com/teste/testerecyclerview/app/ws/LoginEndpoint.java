package br.com.teste.testerecyclerview.app.ws;


import br.com.teste.testerecyclerview.app.dto.LoginDTO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginEndpoint {

    @FormUrlEncoded
    @POST("login/")
    Call<LoginDTO> logarUsuario(@Field("username") String username, @Field("password") String password);

    //SharedPreferences ou AccountManager
}
