package br.com.teste.testerecyclerview.app.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.LoginDTO;
import br.com.teste.testerecyclerview.app.resources.Constantes;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.LoginEndpoint;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends StartNightActivity {

    private LoginDTO loginDTO;
    private SharedPreferencesHelper sharedPreferences;
    private TextInputEditText usernameView, senhaView;
    private TextInputLayout usernameLayout, senhaLayout;
    private CoordinatorLayout coordinatorLayout;
    private Button bt_entrar;
    private TextView bt_cadastrar;
    private View progressBar;
    private Context context;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        context = this;

        usernameView = findViewById(R.id.username);
        senhaView = findViewById(R.id.senha);
        usernameLayout = findViewById(R.id.usernameLayout);
        senhaLayout = findViewById(R.id.senhaLayout);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        progressBar = findViewById(R.id.progressIndeterminateBar);
        bt_entrar = findViewById(R.id.bt_entrar);
        bt_cadastrar = findViewById(R.id.cadastrar);

        senhaView.setOnEditorActionListener(editorAction);

        Button bt_entrar = findViewById(R.id.bt_entrar);
        TextView bt_cadastrar = findViewById(R.id.cadastrar);

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UsuarioCadastrarActivity.class);
                startActivityForResult(i, Constantes.USUARIO_CADASTROU_REQUEST);
            }
        });

        bt_entrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Usuario usuario;
                usuario = new Usuario(
                        usernameView.getText().toString(),
                        senhaView.getText().toString()
                );

                //validando dados
                if (validarFormularioLogin(usuario)) {
                    msgErroSnackBar(coordinatorLayout, "Verifique os campos");
                    return;
                }

                Log.d("LRDG", "usuario: " + usuario.toString());
                logar(usuario);
                //new LoginTask(usuario, context).execute();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constantes.USUARIO_CADASTROU_REQUEST) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intentSessaoExpirou = getIntent();
        if (intentSessaoExpirou.getBooleanExtra("sessaoExpirou", false)) {
            msgErroSnackBar(coordinatorLayout, "Sua sessão expirou, realize o login novamente");
            intentSessaoExpirou.putExtra("sessaoExpirou", false);
        }

        Intent intentLogout = getIntent();
        if (intentLogout.getBooleanExtra("logout", false)) {
            Snackbar.make(coordinatorLayout,"Você saiu do StartNight... Até a próxima :)", Snackbar.LENGTH_LONG).show();
            intentLogout.putExtra("logout", false);
        }
    }

    private EditText.OnEditorActionListener editorAction = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            Usuario usuario = new Usuario();
            usuario.setUsername(usernameView.getText().toString());
            usuario.setSenha(senhaView.getText().toString());

            return validarFormularioLogin(usuario);
        }
    };

    private boolean validarFormularioLogin(Usuario usuario) {

        boolean isOk = false;

        try {
            usuario.validarUsername();
            usernameLayout.setError(null);

        } catch (Exception cause) {
            usernameLayout.setError(cause.getMessage());
            isOk = true;
        }

        try {
            usuario.validarSenha();
            senhaLayout.setError(null);
        } catch (Exception cause) {
            senhaLayout.setError(cause.getMessage());
            isOk = true;
        }

        return isOk;
    }

    private void logar(Usuario usuario) {

        progressBar.setVisibility(View.VISIBLE);
        bt_entrar.setVisibility(View.GONE);
        bt_cadastrar.setVisibility(View.GONE);

        LoginEndpoint endpoint = RetrofitHelper.with(this).createLoginEndpoint();

        Call<LoginDTO> call = endpoint.logarUsuario(
                usuario.getUsername(),
                usuario.getSenha()
        );

        call.enqueue(new Callback<LoginDTO>() {
            @Override
            public void onResponse(@NonNull Call<LoginDTO> call, @NonNull Response<LoginDTO> response) {

                loginDTO = response.body();
                if (response.isSuccessful()) {
                    if (loginDTO != null) {
                        sharedPreferences = new SharedPreferencesHelper(getApplicationContext());
                        sharedPreferences.setToken(loginDTO.getKey());
                        Log.d("LRDG", "Autenticado! Token =" + loginDTO.getKey());

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    try {
                        Gson gson = new Gson();
                        loginDTO = gson.fromJson(response.errorBody().string(), LoginDTO.class);

                        Log.d("LRDG", "loginDTO=" + loginDTO.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("LRDG", "Erro! Sem sucesso no login! Erro: " + response.code());
                    Log.d("LRDG", "loginDTO response: " + loginDTO);
                    Log.d("LRDG", "response toString: " + response.toString());
                    Log.d("LRDG", "response message: " + response.message());

                    usernameLayout.setError(" ");
                    senhaLayout.setError(" ");

                    msgErroSnackBar(coordinatorLayout, loginDTO.getNon_field_errors()[0]);
                    Log.d("LRDG", "Erro no formulário: " + loginDTO.getNon_field_errors()[0]);

                    progressBar.setVisibility(View.GONE);
                    bt_entrar.setVisibility(View.VISIBLE);
                    bt_cadastrar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginDTO> call, @NonNull Throwable t) {
                Log.d("LRDG", "Falha no login!");
                progressBar.setVisibility(View.GONE);
                bt_entrar.setVisibility(View.VISIBLE);
                bt_cadastrar.setVisibility(View.VISIBLE);
            }
        });
    }

}
