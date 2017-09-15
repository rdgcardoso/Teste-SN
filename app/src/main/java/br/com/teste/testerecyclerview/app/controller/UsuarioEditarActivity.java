package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.UsuarioFormularioDTO;
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

        setupToolbar();

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

        dataNascimentoView.setOnEditorActionListener(editorAction);
        dataNascimentoView.addTextChangedListener(Mask.insert(Mask.MaskType.DATA, dataNascimentoView));

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

                if (validarFormularioUsuario(usuario)) {
                    msgErroSnackBar(coordinatorLayout, "Verifique os campos");
                    return;
                }

                final UsuarioDTO usuarioDTO = new UsuarioDTO();
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
                                setResult(RESULT_OK, new Intent());
                                finish();
                            }

                        } else {
                            msgErroSnackBar(coordinatorLayout, "Não foi possível alterar o Perfil");

                            try {
                                Gson gson = new Gson();
                                UsuarioFormularioDTO usuarioFormularioDTO = gson.fromJson(response.errorBody().string(), UsuarioFormularioDTO.class);
                                Log.d("LRDG", "usuarioFormularioDTO=" + usuarioFormularioDTO.toString());

                                if (usuarioFormularioDTO.getUsername() != null) {
                                    usernameLayout.setError(usuarioFormularioDTO.getUsername()[0]);
                                }
                                if (usuarioFormularioDTO.getFirst_name() != null) {
                                    nomeLayout.setError(usuarioFormularioDTO.getFirst_name()[0]);
                                }
                                if (usuarioFormularioDTO.getLast_name() != null) {
                                    sobrenomeLayout.setError(usuarioFormularioDTO.getLast_name()[0]);
                                }
                                if (usuarioFormularioDTO.getEmail() != null) {
                                    emailLayout.setError(usuarioFormularioDTO.getEmail()[0]);
                                }
                                if (usuarioFormularioDTO.getDataNascimento() != null) {
                                    dataNascimentoLayout.setError(usuarioFormularioDTO.getDataNascimento()[0]);
                                }
                                if (usuarioFormularioDTO.getSexo() != null) {
                                    generoSpinner.setError(usuarioFormularioDTO.getSexo()[0]);
                                }
                                if (usuarioFormularioDTO.getNon_field_errors() != null) {
                                    Log.d("LRDG", "Erro no formulário: " + usuarioFormularioDTO.getNon_field_errors()[0]);
                                }

                                Log.d("LRDG", "response sem sucesso: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

    private EditText.OnEditorActionListener editorAction = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

            Usuario usuario = new Usuario();
            usuario.setUsername(usernameView.getText().toString());
            usuario.setNome(nomeView.getText().toString());
            usuario.setSobrenome(sobrenomeView.getText().toString());
            usuario.setEmail(emailView.getText().toString());
            usuario.setDataNascimentoFormatada(dataNascimentoView.getText().toString());
            usuario.setGeneroDescricao(generoSpinner.getSelectedItem().toString());

            return validarFormularioUsuario(usuario);
        }
    };

    private boolean validarFormularioUsuario(Usuario usuario) {

        boolean isOk = false;

        try {
            usuario.validarUsername();
            usernameLayout.setError(null);

        } catch (Exception cause) {
            usernameLayout.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarNome();
            nomeLayout.setError(null);

        } catch (Exception cause) {
            nomeLayout.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarSobrenome();
            sobrenomeLayout.setError(null);

        } catch (Exception cause) {
            sobrenomeLayout.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarEmail();
            emailLayout.setError(null);

        } catch (Exception cause) {
            emailLayout.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarDataNascimento();
            dataNascimentoLayout.setError(null);

        } catch (Exception cause) {
            dataNascimentoLayout.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarGenero();
            generoSpinner.setError(null);

        } catch (Exception cause) {
            generoSpinner.setError(cause.getMessage());
            isOk = true;
        }

        return isOk;
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

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Editar Perfil");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
