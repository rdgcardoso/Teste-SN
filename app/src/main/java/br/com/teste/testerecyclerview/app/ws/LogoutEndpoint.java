package br.com.teste.testerecyclerview.app.ws;


import br.com.teste.testerecyclerview.app.dto.LogoutDTO;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LogoutEndpoint {

    @POST("logout/")
    Call<LogoutDTO> logoutUsuario(@Header("Authorization") String authorization);
}
