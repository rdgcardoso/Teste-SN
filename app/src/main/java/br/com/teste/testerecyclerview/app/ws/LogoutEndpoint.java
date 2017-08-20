package br.com.teste.testerecyclerview.app.ws;


import br.com.teste.testerecyclerview.app.dto.LogoutDTO;
import br.com.teste.testerecyclerview.app.dto.TokenDTO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LogoutEndpoint {

    @POST("logout/")
    Call<LogoutDTO> logoutUsuario(@Header("Authorization") String authorization);
}
