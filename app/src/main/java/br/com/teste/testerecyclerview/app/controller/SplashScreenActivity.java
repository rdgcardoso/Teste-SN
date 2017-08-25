package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.LoginDTO;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.TokenEndpoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferencesHelper sharedPreferences;
    private String erro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferences = new SharedPreferencesHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sharedPreferences.getToken().isEmpty()) {
            //TODO NÃO TENHO TOKEN
            Log.d("LRDG", "Token vazio na Splash=" + sharedPreferences.getToken());
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);

        } else { //TOKEN: VALIDO OU INVÁLIDO?
            TokenEndpoint endpoint = RetrofitHelper.with(getApplicationContext()).createTokenEndpoint();
            Call<LoginDTO> call = endpoint.consultarToken(sharedPreferences.recuperarToken());

            call.enqueue(new Callback<LoginDTO>() {
                Intent i;
                @Override
                public void onResponse(@NonNull Call<LoginDTO> call, @NonNull Response<LoginDTO> response) {
                    if (response.isSuccessful()) {
                        Log.d("LRDG", "Consultado! Token válido=" + sharedPreferences.recuperarToken());
                        i = new Intent(getApplicationContext(), MainActivity.class);
                        Log.d("LRDG", "Abro MainActivity");

                    } else {
                        Log.d("LRDG", "Consultado! Token inválido=" + sharedPreferences.recuperarToken());
                        sharedPreferences.setToken("");
                        i = new Intent(getApplicationContext(), LoginActivity.class);
                        Log.d("LRDG", "Abro LoginActivity");
                    }

                    startActivity(i);
                }

                @Override
                public void onFailure(@NonNull Call<LoginDTO> call, @NonNull Throwable t) {
                    Log.d("LRDG", "Falha ao consultar Token!"+t);
                }
            });
        }
    }
}
