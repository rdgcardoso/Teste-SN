package br.com.teste.testerecyclerview.app.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.task.AutenticarUsuarioTask;


public class LoginActivity extends StartNightActivity {

    private TextView usernameView;
    private TextView passwordView;
    private TextView bt_cadastrar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        Button bt_entrar = (Button) findViewById(R.id.bt_entrar);
        usernameView = (TextView) findViewById(R.id.username);
        passwordView = (TextView) findViewById(R.id.password);
        bt_cadastrar = (TextView) findViewById(R.id.cadastrar);

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
                new AutenticarUsuarioTask(getApplicationContext(), usernameView.getText().toString(), passwordView.getText().toString()).execute();
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
    }
}
