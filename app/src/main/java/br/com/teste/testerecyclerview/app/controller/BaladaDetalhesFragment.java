package br.com.teste.testerecyclerview.app.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.BaladaDTO;
import br.com.teste.testerecyclerview.app.resources.CodigoRetornoHTTP;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.BaladaEndpoint;
import br.com.teste.testerecyclerview.domain.model.Balada;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaladaDetalhesFragment extends Fragment {

    private BaladaDTO baladaDTO;
    private Balada balada;
    private TextView endereco1View, endereco2View, descricaoView, siteView, musicaTipoView, precoMedioView;
    private ProgressBar progressBar;
    private LinearLayout detalhesBaladaContainer;
    private ImageView fotoView;
    private RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balada_detalhes, container, false);

        long id = getArguments().getLong("id");
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this.getContext());

        fotoView = getActivity().findViewById(R.id.foto);
        endereco1View = getActivity().findViewById(R.id.endereco1);
        endereco2View = getActivity().findViewById(R.id.endereco2);
        ratingBar = getActivity().findViewById(R.id.avaliacaoEstrelas);

        descricaoView = view.findViewById(R.id.descricao);
        siteView = view.findViewById(R.id.site);
        musicaTipoView = view.findViewById(R.id.tipoMusica);
        precoMedioView = view.findViewById(R.id.precoMedio);
        progressBar = view.findViewById(R.id.progressIndeterminateBar);
        detalhesBaladaContainer = view.findViewById(R.id.detalhesBaladaContainer);

        progressBar.setVisibility(View.VISIBLE);
        detalhesBaladaContainer.setVisibility(View.GONE);

        consultarBalada(id, sharedPreferencesHelper.recuperarToken());

        return view;
    }

    private void consultarBalada(final long id, String token) {
        BaladaEndpoint endpoint = RetrofitHelper.with(getContext()).createBaladaEndpoint();

        Call<BaladaDTO> call = endpoint.consultarBalada(id, token);

        call.enqueue(new Callback<BaladaDTO>() {
            @Override
            public void onResponse(@NonNull Call<BaladaDTO> call, @NonNull Response<BaladaDTO> response) {

                baladaDTO = response.body();
                if (response.isSuccessful()) {
                    if (baladaDTO != null) {
                        balada = new Balada(
                                id,
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

                        if (balada.getFoto() != null) {
                            Picasso.with(getContext()).load(balada.getFoto()).into(fotoView);
                        }

                        endereco1View.setText(balada.getEndereco1());
                        endereco2View.setText(balada.getEndereco2());
                        descricaoView.setText(balada.getDescricao());
                        siteView.setText(balada.getSite());
                        musicaTipoView.setText(balada.getTipoMusicas());
                        precoMedioView.setText(balada.getPrecoMedio());
                        ratingBar.setRating(balada.getAvaliacao());
                    }
                } else {
                    Log.d("LRDG", "Erro! Sem sucesso no consultar detalhes balada!");
                    CodigoRetornoHTTP.notAuthorized(getContext(), response.code());
                }

                progressBar.setVisibility(View.GONE);
                detalhesBaladaContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<BaladaDTO> call, @NonNull Throwable t) {
                Log.d("LRDG", "Falha no balada detalhes fragment!");
                progressBar.setVisibility(View.GONE);
                detalhesBaladaContainer.setVisibility(View.VISIBLE);
            }
        });
    }
}
