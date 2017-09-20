package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.adapter.BaladaAdapter;
import br.com.teste.testerecyclerview.app.adapter.CustomItemClickListener;
import br.com.teste.testerecyclerview.app.dto.BaladaDTO;
import br.com.teste.testerecyclerview.app.resources.CodigoRetornoHTTP;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.RankingBaladasEndpoint;
import br.com.teste.testerecyclerview.domain.model.Balada;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaladaRankingFragment extends Fragment {

    private SharedPreferencesHelper sharedPreferencesHelper;
    private List<BaladaDTO> baladaDTOList;
    private Balada balada;
    private List<Balada> baladaList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Call<List<BaladaDTO>> call;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balada_ranking, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("StartNight");

        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());

        swipeRefreshLayout = getActivity().findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                carregarBaladaRanking();
            }
        });

        View progressBar = getActivity().findViewById(R.id.progressIndeterminateBar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = getActivity().findViewById(R.id.recycler);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        carregarBaladaRanking();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("LRDG", "BaladaRankingFragment onPause");
        call.cancel();
    }

    private void carregarBaladaRanking() {

        RankingBaladasEndpoint endpoint = RetrofitHelper.with(getContext()).createRankingBaladasEndpoint();

        baladaList = new ArrayList<>();

        call = endpoint.consultarRankingBaladas(sharedPreferencesHelper.recuperarToken());

        call.enqueue(new Callback<List<BaladaDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<BaladaDTO>> call, @NonNull Response<List<BaladaDTO>> response) {

                baladaDTOList = response.body();
                if (response.isSuccessful()) {
                    if (baladaDTOList != null) {
                        Log.d("LRDG", "baladaDTOList preenchido: " + baladaDTOList.toString());

                        for (BaladaDTO baladaDTO : baladaDTOList) {
                            if (baladaDTO.isAtivo()) {
                                balada = new Balada(
                                        baladaDTO.getId(),
                                        baladaDTO.getTipo_musicas(),
                                        baladaDTO.getNome(),
                                        baladaDTO.getDescricao(),
                                        baladaDTO.getCep(),
                                        baladaDTO.getEstado(),
                                        baladaDTO.getCidade(),
                                        baladaDTO.getBairro(),
                                        baladaDTO.getLogradouro(),
                                        baladaDTO.getNumero(),
                                        baladaDTO.getComplemento(),
                                        baladaDTO.getAvaliacao(),
                                        baladaDTO.getSite(),
                                        baladaDTO.getPreco_medio(),
                                        baladaDTO.isAtivo(),
                                        baladaDTO.getFoto()
                                );
                                baladaList.add(balada);
                            }
                        }

                        BaladaAdapter baladaAdapter = new BaladaAdapter(getContext(), (ArrayList<Balada>) baladaList, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                Log.d("LRDG", "Clicado position: " + position);

                                try {
                                    Balada balada;
                                    balada = baladaList.get(position);

                                    Intent i = new Intent(getContext(), BaladaDetalhesActivity.class);
                                    i.putExtra("balada", balada);
                                    startActivity(i);
                                } catch (IndexOutOfBoundsException cause) {
                                    Log.d("LRDG", "Erro ao selecionar balada... Ranking em atualização");
                                }
                            }
                        });
                        recyclerView.setAdapter(baladaAdapter);

                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getContext(), "Ranking atualizado...", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Log.d("LRDG", "Erro em BaladaRankingFragment");
                    CodigoRetornoHTTP.notAuthorized(getContext(), response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BaladaDTO>> call, @NonNull Throwable t) {
                Log.d("LRDG", "Falha em BaladaRankingFragment");
            }

        });
    }
}
