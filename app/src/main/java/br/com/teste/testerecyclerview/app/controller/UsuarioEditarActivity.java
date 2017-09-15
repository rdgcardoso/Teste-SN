package br.com.teste.testerecyclerview.app.controller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.CadastroUsuarioDTO;
import br.com.teste.testerecyclerview.app.dto.UsuarioDTO;
import br.com.teste.testerecyclerview.app.resources.Mask;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.UsuarioEditarEndpoint;
import br.com.teste.testerecyclerview.app.ws.UsuarioEndpoint;
import br.com.teste.testerecyclerview.domain.model.Genero;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class UsuarioEditarActivity extends StartNightActivity {

    private UsuarioDTO usuarioDTO;
    private TextInputEditText usernameView, nomeView, sobrenomeView, emailView, dataNascimentoView;
    private MaterialSpinner generoSpinner;
    private Button bt_salvar;
    private ProgressBar progressBar;
    private ArrayAdapter<String> adapterGenero;
    private TextInputLayout usernameLayout, nomeLayout, sobrenomeLayout, emailLayout, dataNascimentoLayout;
    private CoordinatorLayout coordinatorLayout;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private UsuarioDTO usuarioDTOresponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_editar);

        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        usernameView = findViewById(R.id.username);
        nomeView = findViewById(R.id.nome);
        sobrenomeView = findViewById(R.id.sobrenome);
        emailView = findViewById(R.id.email);
        dataNascimentoView = findViewById(R.id.dataNascimento);
        generoSpinner = findViewById(R.id.genero);

        usernameLayout = (TextInputLayout) findViewById(R.id.usernameLayout);
        nomeLayout = (TextInputLayout) findViewById(R.id.nomeLayout);
        sobrenomeLayout = (TextInputLayout) findViewById(R.id.sobrenomeLayout);
        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        dataNascimentoLayout = (TextInputLayout) findViewById(R.id.dataNascimentoLayout);

        bt_salvar = findViewById(R.id.bt_salvar);
        progressBar = findViewById(R.id.progressIndeterminateBar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        //dataNascimentoView.setOnEditorActionListener(editorAction);
        //dataNascimentoView.addTextChangedListener(Mask.insert(Mask.MaskType.DATA, dataNascimentoView));

        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Usuario usuario = new Usuario();
                usuario.setUsername(usernameView.getText().toString());
                usuario.setNome(nomeView.getText().toString());
                usuario.setSobrenome(sobrenomeView.getText().toString());
                usuario.setEmail(emailView.getText().toString());
                usuario.setDataNascimentoFormatada(dataNascimentoView.getText().toString());
                usuario.setGeneroDescricao(generoSpinner.getSelectedItem().toString());

                Log.d("LRDG", "usuario=" + (usuario.toString()));

                //validando dados
                /*if (validarFormularioUsuario(usuario)) {

                    msgErroSnackBar(coordinatorLayout, "Verifique os campos");
                    return;
                }*/

                UsuarioDTO usuarioDTO = new UsuarioDTO();
                usuarioDTO.setUsername(usuario.getUsername());
                usuarioDTO.setFirst_name(usuario.getNome());
                usuarioDTO.setLast_name(usuario.getSobrenome());
                usuarioDTO.setEmail(usuario.getEmail());
                usuarioDTO.setSexo(usuario.getGeneroId());
                usuarioDTO.setDataNascimento(usuario.getDataNascimento());

                Gson gson = new GsonBuilder().create();
                String jsonUsuarioDTO = gson.toJson(usuarioDTO);

                Log.d("LRDG", "jsonUsuarioDTO usuarioDTO=" + jsonUsuarioDTO);

                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonUsuarioDTO);

                UsuarioEditarEndpoint endpoint = RetrofitHelper.with(getApplicationContext()).createUsuarioEditarEndpoint();
                Call<UsuarioDTO> call = endpoint.editarUsuario(
                        sharedPreferencesHelper.recuperarToken(),
                        body
                );

                call.enqueue(new Callback<UsuarioDTO>() {
                    @Override
                    public void onResponse(@NonNull Call<UsuarioDTO> call, @NonNull Response<UsuarioDTO> response) {

                        usuarioDTOresponse = response.body();
                        if (response.isSuccessful()) {
                            if (usuarioDTOresponse != null) {
                                Log.d("LRDG", "code1: " + response.code());
                                Log.d("LRDG", "response não nulo" + usuarioDTOresponse.toString());
                                finish();
                            }

                        } else {
                            Log.d("LRDG", "code2: " + response.code());
                            try {
                                msgErroSnackBar(coordinatorLayout, "Não foi possível alterar o Perfil");
                                Log.d("LRDG", "response sem sucesso: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "Falha ao alterar usuário!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UsuarioDTO> call, @NonNull Throwable t) {
                        Log.d("LRDG", "Falha no alterar usuario!");
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Genero genero = new Genero();
        adapterGenero = new ArrayAdapter<>(
                this,
                R.layout.spinner_list_style,
                genero.getGenerosDescricaoList()
        );

        adapterGenero.setDropDownViewResource(R.layout.spinner_dropdown_item);
        generoSpinner.setAdapter(adapterGenero);

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

                        Usuario usuario = new Usuario();
                        usuario.setUsername(usuarioDTO.getUsername());
                        usuario.setNome(usuarioDTO.getFirst_name());
                        usuario.setSobrenome(usuarioDTO.getLast_name());
                        usuario.setEmail(usuarioDTO.getEmail());
                        usuario.setDataNascimentoFormatada(usuarioDTO.getDataNascimentoFormatada());
                        usuario.setGeneroId(usuarioDTO.getSexo());

                        Log.d("LRDG", "UsuarioDTO preenchido: " + usuarioDTO.toString());
                        Log.d("LRDG", "UserInfo preenchido: " + usuarioDTO.getUserInfo().toString());
                        Log.d("LRDG", "Usuario preenchido: " + usuario.toString());


                        usernameView.setText(usuario.getUsername());
                        nomeView.setText(usuario.getNome());
                        sobrenomeView.setText(usuario.getSobrenome());
                        emailView.setText(usuario.getEmail());
                        dataNascimentoView.setText(usuario.getDataNascimentoFormatada());
                        generoSpinner.setSelection(adapterGenero.getPosition(usuario.getGeneroDescricao()) + 1);

                        Log.d("LRDG", "usuario.getGeneroDescricao() = " + usuario.getGeneroDescricao());

                        //generoSpinner.setSelection(adapterGenero.getPosition(usuarioDTO.getSexo()));
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
