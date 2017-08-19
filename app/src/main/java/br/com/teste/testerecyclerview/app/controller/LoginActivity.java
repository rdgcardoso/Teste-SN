package br.com.teste.testerecyclerview.app.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.TokenDTO;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.LoginEndpoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends StartNightActivity {

    private Button bt_entrar;
    private TextView usernameView;
    private TextView passwordView;
    private TokenDTO dto;
    private SharedPreferencesHelper sharedPreferences;
    private Context context;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        context = this;
        bt_entrar = (Button) findViewById(R.id.bt_entrar);
        usernameView = (TextView) findViewById(R.id.username);
        passwordView = (TextView) findViewById(R.id.password);
        sharedPreferences = new SharedPreferencesHelper(this);

        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginEndpoint endpoint = RetrofitHelper.with(getApplicationContext()).createLoginEndpoint();
                Call<TokenDTO> call = endpoint.logarUsuario(usernameView.getText().toString(), passwordView.getText().toString());

                call.enqueue(new Callback<TokenDTO>() {
                    @Override
                    public void onResponse(Call<TokenDTO> call, Response<TokenDTO> response) {

                        dto = response.body();
                        if (response.isSuccessful()) {

                            if (dto == null) {
                                return;
                            }

                            sharedPreferences.salvarTokenCache(dto.getKey());
                            Log.d("LRDG", "Autenticado! Token " + dto.getKey());

                            Intent i = new Intent(context, MainActivity.class);
                            startActivity(i);

                        }
                        if (!response.isSuccessful()) {
                            Toast.makeText(
                                    context,
                                    "Usuário e/ou Senha Inválidos!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenDTO> call, Throwable t) {
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO: MUDAR P/ ! DIFERENTE
        if (!sharedPreferences.recuperarTokenCache().equals("")) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
}
