package br.com.teste.testerecyclerview.app.util;

import android.content.Context;

import br.com.teste.testerecyclerview.app.ws.BaladaEndpoint;
import br.com.teste.testerecyclerview.domain.model.Balada;
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

    public BaladaEndpoint createBaladaEndpoint() {
        return retrofit.create(BaladaEndpoint.class);
    }

    public static RetrofitHelper with (Context context) {
        return new RetrofitHelper(context);
    }
}
