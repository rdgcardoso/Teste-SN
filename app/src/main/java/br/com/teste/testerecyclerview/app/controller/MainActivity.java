package br.com.teste.testerecyclerview.app.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.adapter.BaladaAdapter;
import br.com.teste.testerecyclerview.app.adapter.CustomItemClickListener;
import br.com.teste.testerecyclerview.app.task.ConsultaBaladasTask;
import br.com.teste.testerecyclerview.domain.model.Balada;


public class MainActivity extends AppCompatActivity {

    private List<Balada> baladaList;
    private RecyclerView recyclerView;
    private BaladaAdapter baladaAdapter;
    private Context context;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id = "";

        Log.i("LRDG", "passou!");
        new ConsultaBaladasTask(MainActivity.this, id).execute();

        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        baladaList = new ArrayList<>();
        baladaList = getBaladas();

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        baladaAdapter = new BaladaAdapter(this, (ArrayList<Balada>) baladaList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("LRDG", "Clicado: " + position);
                long id;
                id = baladaList.get(position).getId();
                Log.d("LRDG", "ID: " + id);

                Bundle bundle = new Bundle();
                bundle.putLong("id", id);
                Intent i = new Intent(context, BaladaDetalhesActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });
        recyclerView.setAdapter(baladaAdapter);
    }

    public List<Balada> getBaladas() {
        List<Balada> lista = new ArrayList<>();
        lista.add(new Balada(1, "Balada 1", "Rua 1", "São Paulo - SP", "Pagode", "5.0", "50,00", 0));
        lista.add(new Balada(2, "Balada 2", "Rua 2", "Rio de Janeiro - RJ", "Sertanejo", "3.0", "30,00", 1));
        lista.add(new Balada(3, "Balada 3", "Rua 3", "São Paulo - SP", "Samba", "4.5", "25,00", 2));
        lista.add(new Balada(4, "Balada 4", "Rua 1", "São Paulo - SP", "Pagode", "5.0", "50,00", 0));
        lista.add(new Balada(5, "Balada 5", "Rua 2", "Rio de Janeiro - RJ", "Sertanejo", "3.0", "30,00", 1));
        lista.add(new Balada(6, "Balada 6", "Rua 3", "São Paulo - SP", "Samba", "4.5", "25,00", 2));
        lista.add(new Balada(7, "Balada 7", "Rua 1", "São Paulo - SP", "Pagode", "5.0", "50,00", 0));
        lista.add(new Balada(8, "Balada 8", "Rua 2", "Rio de Janeiro - RJ", "Sertanejo", "3.0", "30,00", 1));
        lista.add(new Balada(9, "Balada 9", "Rua 3", "São Paulo - SP", "Samba", "4.5", "25,00", 2));
        lista.add(new Balada(10, "Balada 10", "Rua 1", "São Paulo - SP", "Pagode", "5.0", "50,00", 0));
        lista.add(new Balada(11, "Balada 11", "Rua 2", "Rio de Janeiro - RJ", "Sertanejo", "3.0", "30,00", 1));
        lista.add(new Balada(12, "Balada 12", "Rua 3", "São Paulo - SP", "Samba", "4.5", "25,00", 2));

        return lista;
    }
}
