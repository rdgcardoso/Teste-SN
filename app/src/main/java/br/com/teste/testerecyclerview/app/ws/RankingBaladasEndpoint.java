package br.com.teste.testerecyclerview.app.ws;


import java.util.List;

import br.com.teste.testerecyclerview.app.dto.BaladaDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface RankingBaladasEndpoint {

    //@Headers("Authorization: Token 604817b27f53f641a3e3bfce3646beb30587be30")
    @GET("baladas/ranking")
    Call<List<BaladaDTO>> consultarRankingBaladas(@Header("Authorization") String authorization);
}
