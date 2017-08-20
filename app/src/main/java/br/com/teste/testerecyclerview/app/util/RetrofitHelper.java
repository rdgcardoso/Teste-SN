package br.com.teste.testerecyclerview.app.util;

import android.content.Context;

import br.com.teste.testerecyclerview.app.ws.BaladaEndpoint;
import br.com.teste.testerecyclerview.app.ws.LoginEndpoint;
import br.com.teste.testerecyclerview.app.ws.LogoutEndpoint;
import br.com.teste.testerecyclerview.app.ws.RankingBaladasEndpoint;
import br.com.teste.testerecyclerview.app.ws.UsuarioEndpoint;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;

public final class RetrofitHelper {

    private Retrofit retrofit;
    private Context context;

    private RetrofitHelper(Context context) {
        super();
        this.context = context;
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://startnight.com.br/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RankingBaladasEndpoint createRankingBaladasEndpoint() {
        return retrofit.create(RankingBaladasEndpoint.class);
    }

    public BaladaEndpoint createBaladaEndpoint() {
        return retrofit.create(BaladaEndpoint.class);
    }

    public UsuarioEndpoint createUsuarioEndpoint() {
        return retrofit.create(UsuarioEndpoint.class);
    }

    public LoginEndpoint createLoginEndpoint() {
        return retrofit.create(LoginEndpoint.class);
    }

    public LogoutEndpoint createLogoutEndpoint() {
        return retrofit.create(LogoutEndpoint.class);
    }

    public static RetrofitHelper with (Context context) {
        return new RetrofitHelper(context);
    }


}
