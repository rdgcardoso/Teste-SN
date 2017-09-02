package br.com.teste.testerecyclerview.app.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.LoginDTO;
import br.com.teste.testerecyclerview.app.task.LoginTask;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.domain.model.Usuario;


public class LoginActivity extends StartNightActivity {

    private LoginDTO loginDTO;
    private SharedPreferencesHelper sharedPreferences;
    private TextInputEditText usernameView, senhaView;
    private TextInputLayout usernameLayout, senhaLayout;
    private CoordinatorLayout coordinatorLayout;
    Context context;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        context = this;

        usernameView = (TextInputEditText) findViewById(R.id.username);
        senhaView = (TextInputEditText) findViewById(R.id.senha);
        usernameLayout = (TextInputLayout) findViewById(R.id.usernameLayout);
        senhaLayout = (TextInputLayout) findViewById(R.id.senhaLayout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        senhaView.setOnEditorActionListener(editorAction);

        Button bt_entrar = (Button) findViewById(R.id.bt_entrar);
        TextView bt_cadastrar = (TextView) findViewById(R.id.cadastrar);

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UsuarioCadastrarActivity.class);
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
                new LoginTask(usuario, context).execute();

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

        if (getIntent().getBooleanExtra("logout", false)) {
            Snackbar.make(coordinatorLayout,"Você saiu do StartNight... Até a próxima :)", Snackbar.LENGTH_LONG).show();
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

}
