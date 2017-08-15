package br.com.teste.testerecyclerview.app.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.task.ConsultaBaladasTask;
import br.com.teste.testerecyclerview.domain.model.Balada;


public class MainActivity extends AppCompatActivity {

    private List<Balada> baladaList;
    private Context context;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id = "";

        Log.i("LRDG", "passou!");

        new ConsultaBaladasTask(MainActivity.this, id).execute();

    }

}
