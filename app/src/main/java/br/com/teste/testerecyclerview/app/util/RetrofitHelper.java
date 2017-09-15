package br.com.teste.testerecyclerview.app.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.teste.testerecyclerview.app.controller.LoginActivity;
import br.com.teste.testerecyclerview.app.ws.BaladaEndpoint;
import br.com.teste.testerecyclerview.app.ws.CadastrarUsuarioEndpoint;
import br.com.teste.testerecyclerview.app.ws.LoginEndpoint;
import br.com.teste.testerecyclerview.app.ws.LogoutEndpoint;
import br.com.teste.testerecyclerview.app.ws.RankingBaladasEndpoint;
import br.com.teste.testerecyclerview.app.ws.TokenEndpoint;
import br.com.teste.testerecyclerview.app.ws.UsuarioEditarEndpoint;
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

    public TokenEndpoint createTokenEndpoint() {
        return retrofit.create(TokenEndpoint.class);
    }

    public CadastrarUsuarioEndpoint createCadastrarUsuarioEndpoint() {
        return retrofit.create(CadastrarUsuarioEndpoint.class);
    }

    public UsuarioEditarEndpoint createUsuarioEditarEndpoint() {
        return retrofit.create(UsuarioEditarEndpoint.class);
    }

    public static RetrofitHelper with (Context context) {
        return new RetrofitHelper(context);
    }
}
