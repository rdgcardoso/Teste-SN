package br.com.teste.testerecyclerview.app.ws;


import java.util.List;

import br.com.teste.testerecyclerview.app.dto.BaladaDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface BaladaEndpoint {

    @Headers("Authorization: Token eea8622e6a6159eac2fa9365e434f2d5fcc804f5")
    @GET("baladas/{id}")
    Call<BaladaDTO> consultarBalada(@Path("id") long id);
}
