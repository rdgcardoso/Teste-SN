package br.com.teste.testerecyclerview.app.util;

import android.content.Context;

import br.com.teste.testerecyclerview.app.ws.RankingBaladaEndpoint;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitHelper {

    private Retrofit retrofit;
    private Context context;

    public RetrofitHelper(Context context) {
        super();
        this.context = context;
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://startnight.com.br/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RankingBaladaEndpoint createBaladaEndpoint() {
        return retrofit.create(RankingBaladaEndpoint.class);
    }

    public static RetrofitHelper with (Context context) {
        return new RetrofitHelper(context);
    }
}
