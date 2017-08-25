package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.LoginDTO;
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
    private TextInputLayout usernameContainerView, senhaContainerView;
    private CoordinatorLayout coordinatorLayout;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        usernameView = (TextInputEditText) findViewById(R.id.username);
        senhaView = (TextInputEditText) findViewById(R.id.senha);
        usernameContainerView = (TextInputLayout) findViewById(R.id.usernameContainer);
        senhaContainerView = (TextInputLayout) findViewById(R.id.senhaContainer);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        senhaView.setOnEditorActionListener(editorAction);

        Button bt_entrar = (Button) findViewById(R.id.bt_entrar);
        TextView bt_cadastrar = (TextView) findViewById(R.id.cadastrar);

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CadastrarUsuarioActivity.class);
                startActivityForResult(i, USUARIO_CADASTROU_REQUEST);
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

                LoginEndpoint endpoint = RetrofitHelper.with(getApplicationContext()).createLoginEndpoint();
                Call<LoginDTO> call = endpoint.logarUsuario(
                        usuario.getUsername(),
                        usuario.getSenha()
                );

                call.enqueue(new Callback<LoginDTO>() {
                    @Override
                    public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {

                        loginDTO = response.body();
                        Log.d("LRDG", "response: " + response.body());
                        Log.d("LRDG", " loginDTO response: " + loginDTO);

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

                                if (loginDTO.getNon_field_errors() != null) {
                                    usernameContainerView.setError(" ");
                                    senhaContainerView.setError(" ");
                                    msgErroSnackBar(coordinatorLayout, loginDTO.getNon_field_errors()[0]);
                                    Log.d("LRDG", "Erro no formulário: " + loginDTO.getNon_field_errors()[0]);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("LRDG", "Erro! Sem sucesso no login! Erro: " + response.code());
                            Log.d("LRDG", "loginDTO response: " + loginDTO);
                            Log.d("LRDG", "response toString: " + response.toString());
                            Log.d("LRDG", "response message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginDTO> call, @NonNull Throwable t) {
                        Log.d("LRDG", "Falha ao logar!");
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == USUARIO_CADASTROU_REQUEST) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getBooleanExtra("sessaoExpirou", false)) {
            msgErroSnackBar(coordinatorLayout, "Sua sessão expirou, realize o login novamente");
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
            usernameContainerView.setError("");

        } catch (Exception cause) {
            usernameContainerView.setError(cause.getMessage());
            isOk = true;
        }

        try {
            usuario.validarSenha();
            senhaContainerView.setError("");

        } catch (Exception cause) {
            senhaContainerView.setError(cause.getMessage());
            isOk = true;
        }

        return isOk;
    }


}
