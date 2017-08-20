package br.com.teste.testerecyclerview.app.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.task.AutenticarUsuarioTask;


public class LoginActivity extends StartNightActivity {

    private TextView usernameView;
    private TextView passwordView;
    private Context context;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        Button bt_entrar = (Button) findViewById(R.id.bt_entrar);
        usernameView = (TextView) findViewById(R.id.username);
        passwordView = (TextView) findViewById(R.id.password);
        context = this;

        bt_entrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new AutenticarUsuarioTask(context, usernameView.getText().toString(), passwordView.getText().toString()).execute();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
