package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.task.ConsultarBaladaTask;
import br.com.teste.testerecyclerview.domain.model.Balada;

public class BaladaDetalhesActivity extends AppCompatActivity {

    private Balada balada;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_balada);

        Intent i = getIntent();
        balada = (Balada) i.getSerializableExtra("balada");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(balada.getNome());
    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageView fotoView = (ImageView) findViewById(R.id.foto);
        TextView endereco1View = (TextView) findViewById(R.id.endereco1);
        TextView endereco2View = (TextView) findViewById(R.id.endereco2);
        TextView descricaoView = (TextView) findViewById(R.id.descricao);
        TextView siteView = (TextView) findViewById(R.id.site);
        TextView musicaTipoView = (TextView) findViewById(R.id.tipoMusica);
        TextView precoMedioView = (TextView) findViewById(R.id.precoMedio);
        RatingBar avaliacaoView = (RatingBar) findViewById(R.id.avaliacaoEstrelas);

        Picasso.with(this).load(balada.getFoto()).into(fotoView);
        endereco1View.setText(balada.getEndereco1());
        endereco2View.setText(balada.getEndereco2());
        descricaoView.setText(balada.getDescricao());
        siteView.setText(balada.getSite());
        musicaTipoView.setText(balada.getTipoMusicas());
        precoMedioView.setText(balada.getPrecoMedio());
        avaliacaoView.setRating(balada.getAvaliacao());

        new ConsultarBaladaTask(this, balada.getId()).execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
