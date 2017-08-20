package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.task.AutenticarUsuarioTask;
import br.com.teste.testerecyclerview.app.task.ConsultarUsuarioTask;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;

public class SplashScreenActivity extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    }

    @Override
    protected void onResume() {
        super.onResume();


        final SharedPreferencesHelper sharedPreferences = new SharedPreferencesHelper(this);
        token = sharedPreferences.getToken();
        Handler handler = new Handler();

        //delay da splash
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //SE EU NAO TIVER TOKEN
                Log.d("LRDG", "Token Splash=" + token);
                if (token.isEmpty()) {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        }, 2000);
    }
}
