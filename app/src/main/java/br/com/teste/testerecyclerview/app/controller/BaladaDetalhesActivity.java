package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import br.com.teste.testerecyclerview.R;

public class BaladaDetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_balada);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        String id = bundle.getString("id");

        Toast.makeText(this, "Id selecionado = " +id, Toast.LENGTH_SHORT).show();
    }
}
