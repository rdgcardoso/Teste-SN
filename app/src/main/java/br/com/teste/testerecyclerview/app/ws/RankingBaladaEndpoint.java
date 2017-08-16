package br.com.teste.testerecyclerview.app.ws;


import java.util.List;

import br.com.teste.testerecyclerview.app.dto.BaladaDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface RankingBaladaEndpoint {

    @Headers("Authorization: Token eea8622e6a6159eac2fa9365e434f2d5fcc804f5")
    //@GET("baladas/ranking{id}")
    @GET("baladas/ranking")
    //Call<List<BaladaDTO>> consultarBaladas(@Path("id") String id);
    Call<List<BaladaDTO>> consultarBaladas();
}
