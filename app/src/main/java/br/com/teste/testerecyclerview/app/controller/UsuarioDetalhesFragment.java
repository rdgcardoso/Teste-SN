package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.UsuarioDTO;
import br.com.teste.testerecyclerview.app.resources.CodigoRetornoHTTP;
import br.com.teste.testerecyclerview.app.resources.Constantes;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.UsuarioEndpoint;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioDetalhesFragment extends Fragment {

    private SharedPreferencesHelper sharedPreferencesHelper;
    private CoordinatorLayout coordinatorLayout;
    private TextView nomeCompletoView, idadeView, usernameView, emailView, generoView;
    private ImageView imageView, imageBlurView;
    private ProgressBar progressBar;
    private UsuarioDTO usuarioDTO;
    private Usuario usuario;
    private View detalhesUsuarioContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usuario_detalhes, container, false);

        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());
        nomeCompletoView = view.findViewById(R.id.nomeCompleto);
        idadeView = view.findViewById(R.id.idade);
        usernameView = view.findViewById(R.id.username);
        emailView = view.findViewById(R.id.email);
        generoView = view.findViewById(R.id.genero);
        progressBar = view.findViewById(R.id.progressIndeterminateBar);
        detalhesUsuarioContainer = view.findViewById(R.id.detalhesUsuarioContainer);
        imageView = view.findViewById(R.id.profile_image);
        imageBlurView = view.findViewById(R.id.profile_imageBlur);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        progressBar = view.findViewById(R.id.progressIndeterminateBar);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), UsuarioEditarActivity.class);
                startActivityForResult(i, Constantes.USUARIO_EDITOU_REQUEST);
            }
        });

        consultarUsuario();

        return view;
    }

    public void consultarUsuario() {

        progressBar.setVisibility(View.VISIBLE);

        UsuarioEndpoint endpoint = RetrofitHelper.with(getContext()).createUsuarioEndpoint();

        Call<UsuarioDTO> call = endpoint.consultarUsuario(sharedPreferencesHelper.recuperarToken());

        call.enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(@NonNull Call<UsuarioDTO> call, @NonNull Response<UsuarioDTO> response) {

                usuarioDTO = response.body();
                if (response.isSuccessful()) {
                    if (usuarioDTO != null) {
                        usuario = new Usuario(
                                usuarioDTO.getId(),
                                usuarioDTO.getUsername(),
                                usuarioDTO.getEmail(),
                                usuarioDTO.getFirst_name(),
                                usuarioDTO.getLast_name(),
                                usuarioDTO.getFoto(),
                                usuarioDTO.getSexo(),
                                usuarioDTO.getDataNascimentoFormatada()
                        );

                        Log.d("LRDG", "=usuario na consulta = " + usuario.toString());
                        Log.d("LRDG", "=usuarioDTO na consulta = " + usuarioDTO.toString());

                        nomeCompletoView.setText(usuario.getNomeCompleto() + ",");
                        idadeView.setText(usuario.getIdade() + " anos");
                        usernameView.setText(usuario.getUsername());
                        emailView.setText(usuario.getEmail());
                        generoView.setText(usuario.getGeneroDescricao());

                        if (usuario.getFoto() != null) {
                            Picasso.with(getContext()).load(usuario.getFoto()).into(imageView);
                            Picasso.with(getContext()).load(usuario.getFoto()).into(imageBlurView);
                        }
                    }
                } else {
                    Log.d("LRDG", "Erro! Sem sucesso no logout!");
                    CodigoRetornoHTTP.notAuthorized(getContext(), response.code());
                }

                progressBar.setVisibility(View.GONE);
                detalhesUsuarioContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<UsuarioDTO> call, @NonNull Throwable t) {
                Log.d("LRDG", "Falha ao carregar UsuarioDetalhes!");
                progressBar.setVisibility(View.GONE);
                detalhesUsuarioContainer.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == Constantes.USUARIO_EDITOU_REQUEST) {
            consultarUsuario();
            Snackbar.make(coordinatorLayout, "Alterações salvas com sucesso", Snackbar.LENGTH_SHORT).show();
        }
    }
}
