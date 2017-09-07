package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.LogoutDTO;
import br.com.teste.testerecyclerview.app.dto.UsuarioDTO;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.LogoutEndpoint;
import br.com.teste.testerecyclerview.app.ws.UsuarioEndpoint;
import br.com.teste.testerecyclerview.domain.model.Genero;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioEditarActivity extends StartNightActivity {

    private SharedPreferencesHelper sharedPreferencesHelper;
    private UsuarioDTO usuarioDTO;
    private TextInputEditText usuarioView;
    private TextInputEditText nomeView;
    private TextInputEditText sobrenomeView;
    private TextInputEditText emailView;
    private TextInputEditText dataNascimentoView;
    private MaterialSpinner generoView;
    private Button bt_salvar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_editar);

        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        usuarioView = findViewById(R.id.username);
        nomeView = findViewById(R.id.nome);
        sobrenomeView = findViewById(R.id.sobrenome);
        emailView = findViewById(R.id.email);
        dataNascimentoView = findViewById(R.id.dataNascimento);
        generoView = findViewById(R.id.genero);
        bt_salvar = findViewById(R.id.bt_salvar);
        progressBar = findViewById(R.id.progressIndeterminateBar);


    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayAdapter<Genero> adapterGenero = new ArrayAdapter<>(
                this,
                R.layout.spinner_list_style,
                Genero.values()
        );
        adapterGenero.setDropDownViewResource(R.layout.spinner_dropdown_item);
        generoView.setAdapter(adapterGenero);

        carregarDadosUsuario();

        bt_salvar.setVisibility(View.GONE);
    }

    private void carregarDadosUsuario() {
        UsuarioEndpoint endpoint = RetrofitHelper.with(this).createUsuarioEndpoint();

        Log.d("LRDG", "token agora: " + sharedPreferencesHelper.recuperarToken());
        Call<UsuarioDTO> call = endpoint.consultarUsuario(sharedPreferencesHelper.recuperarToken());

        call.enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(@NonNull Call<UsuarioDTO> call, @NonNull Response<UsuarioDTO> response) {

                usuarioDTO = response.body();
                if (response.isSuccessful()) {
                    if (usuarioDTO != null) {
                        Log.d("LRDG", "UsuarioDTO preenchido: " + usuarioDTO.toString());

                        usuarioView.setText(usuarioDTO.getUsername());
                        nomeView.setText(usuarioDTO.getFirst_name());
                        sobrenomeView.setText(usuarioDTO.getLast_name());
                        emailView.setText(usuarioDTO.getEmail());
                        dataNascimentoView.setText(usuarioDTO.getDataNascimentoFormatada());
                    }
                } else {
                    Log.d("LRDG", "Erro!");
                }

                bt_salvar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<UsuarioDTO> call, @NonNull Throwable t) {
                Log.d("LRDG", "Falha!");
                bt_salvar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

        });
    }
}
