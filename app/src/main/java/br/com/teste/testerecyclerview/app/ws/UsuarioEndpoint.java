package br.com.teste.testerecyclerview.app.ws;


import java.util.List;

import br.com.teste.testerecyclerview.app.dto.BaladaDTO;
import br.com.teste.testerecyclerview.app.dto.UsuarioDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface UsuarioEndpoint {

    @GET("user")
    Call<UsuarioDTO> consultarUsuario(@Header("Authorization") String authorization);
}
