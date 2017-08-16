package br.com.teste.testerecyclerview.app.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.task.ConsultaBaladasTask;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ConsultaBaladasTask(this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
