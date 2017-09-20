package br.com.teste.testerecyclerview.app.task.apagar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.adapter.BaladaAdapter;
import br.com.teste.testerecyclerview.app.adapter.CustomItemClickListener;
import br.com.teste.testerecyclerview.app.controller.BaladaDetalhesActivity;
import br.com.teste.testerecyclerview.app.dto.BaladaDTO;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.ws.RankingBaladasEndpoint;
import br.com.teste.testerecyclerview.domain.model.Balada;
import br.com.teste.testerecyclerview.app.resources.CodigoRetornoHTTP;
import retrofit2.Call;
import retrofit2.Response;

public class ConsultarRankingBaladasTask extends AsyncTask<Void, Void, List<Balada>> {

    private Context context;
    private AppCompatActivity activity;
    private RankingBaladasEndpoint endpoint;
    private View view;
    private String token;
    private int responseCode;
    private View progressBar;
    private TextView msgProgressBar;

    public ConsultarRankingBaladasTask(Context context, View view, String token) {
        this.context = context;
        this.view = view;
        this.token = token;
    }

    public ConsultarRankingBaladasTask(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @Override //Pré execucao
    protected void onPreExecute() {
        Log.i("LRDG", "Pré execução ConsultarRankingBaladasTask");
    }

    @Override //Execução
    protected List<Balada> doInBackground(Void... voids) {

        Log.i("LRDG", "Execução ConsultarRankingBaladasTask");
        Log.i("LRDG", "Token ConsultarRankingBaladasTask: " + token);

        endpoint = RetrofitHelper.with(context).createRankingBaladasEndpoint();

        List<BaladaDTO> dtoList;
        List<Balada> baladaList;
        Balada balada;

        baladaList = new ArrayList<>();

        try {

            Call<List<BaladaDTO>> call = endpoint.consultarRankingBaladas(token);
            Response<List<BaladaDTO>> response = call.execute();

            if (response.isSuccessful()) {
                dtoList = response.body();

                if (dtoList != null) {
                    for (BaladaDTO dto : dtoList) {
                        if (dto.isAtivo()) {
                            balada = new Balada(
                                    dto.getId(),
                                    dto.getTipo_musicas(),
                                    dto.getNome(),
                                    dto.getDescricao(),
                                    dto.getCep(),
                                    dto.getEstado(),
                                    dto.getCidade(),
                                    dto.getBairro(),
                                    dto.getLogradouro(),
                                    dto.getNumero(),
                                    dto.getComplemento(),
                                    dto.getAvaliacao(),
                                    dto.getSite(),
                                    dto.getPreco_medio(),
                                    dto.isAtivo(),
                                    dto.getFoto()
                            );
                            baladaList.add(balada);
                        }
                    }
                }
            } else {
                Log.i("LRDG", "Erro ConsultarRankingBaladasTask");
                responseCode = response.code();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return baladaList;
    }

    @Override
    protected void onPostExecute(final List<Balada> baladaList) {
        Log.i("LRDG", "Pós execução ConsultarRankingBaladasTask");

        if (CodigoRetornoHTTP.notAuthorized(context, responseCode)) {
            return;
        }

        activity = (AppCompatActivity) context;
        RecyclerView recyclerView = activity.findViewById(R.id.recycler);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        BaladaAdapter baladaAdapter;
        baladaAdapter = new BaladaAdapter(context, (ArrayList<Balada>) baladaList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("LRDG", "Clicado position: " + position);

                Balada balada;
                balada = baladaList.get(position);

                Intent i = new Intent(context, BaladaDetalhesActivity.class);
                i.putExtra("balada", balada);
                activity.startActivity(i);
            }
        });
        recyclerView.setAdapter(baladaAdapter);

        if (view != null) {
            SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, "Ranking atualizado...", Toast.LENGTH_SHORT).show();
        } else {
            progressBar = activity.findViewById(R.id.progressIndeterminateBar);
            progressBar.setVisibility(View.GONE);
        }
    }
}
