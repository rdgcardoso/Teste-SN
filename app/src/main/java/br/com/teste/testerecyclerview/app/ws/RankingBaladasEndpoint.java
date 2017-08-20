package br.com.teste.testerecyclerview.app.ws;


import java.util.List;

import br.com.teste.testerecyclerview.app.dto.BaladaDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface RankingBaladasEndpoint {

    @GET("baladas/ranking")
    Call<List<BaladaDTO>> consultarRankingBaladas(@Header("Authorization") String authorization);
}
