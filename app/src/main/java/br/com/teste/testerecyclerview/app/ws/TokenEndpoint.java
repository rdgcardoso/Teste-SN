package br.com.teste.testerecyclerview.app.ws;


import br.com.teste.testerecyclerview.app.dto.LoginDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TokenEndpoint {

    @GET("user")
    Call<LoginDTO> consultarToken(@Header("Authorization") String authorization);
}
