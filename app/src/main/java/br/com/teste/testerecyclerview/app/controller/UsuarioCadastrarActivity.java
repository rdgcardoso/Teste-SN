package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.UsuarioFormularioDTO;
import br.com.teste.testerecyclerview.app.resources.Mask;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.CadastrarUsuarioEndpoint;
import br.com.teste.testerecyclerview.domain.model.Genero;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioCadastrarActivity extends StartNightActivity {

    private TextInputEditText usernameView, nomeView, sobrenomeView, emailView, dataNascimentoView, senhaView, senhaConfirmacaoView;
    private MaterialSpinner generoSpinner;
    private TextInputLayout usernameLayout, nomeLayout, sobrenomeLayout, emailLayout, dataNascimentoLayout, senhaLayout, senhaConfirmacaoLayout;
    private CoordinatorLayout coordinatorLayout;
    private UsuarioFormularioDTO usuarioFormularioDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        setupToolbar();

        Button bt_cadastrar = (Button) findViewById(R.id.bt_cadastrar);

        usernameView = (TextInputEditText) findViewById(R.id.username);
        nomeView = (TextInputEditText) findViewById(R.id.nome);
        sobrenomeView = (TextInputEditText) findViewById(R.id.sobrenome);
        emailView = (TextInputEditText) findViewById(R.id.email);
        dataNascimentoView = (TextInputEditText) findViewById(R.id.dataNascimento);
        generoSpinner = (MaterialSpinner) findViewById(R.id.genero);
        senhaView = (TextInputEditText) findViewById(R.id.senha);
        senhaConfirmacaoView = (TextInputEditText) findViewById(R.id.senhaConfirmacao);

        usernameLayout = (TextInputLayout) findViewById(R.id.usernameLayout);
        nomeLayout = (TextInputLayout) findViewById(R.id.nomeLayout);
        sobrenomeLayout = (TextInputLayout) findViewById(R.id.sobrenomeLayout);
        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        dataNascimentoLayout = (TextInputLayout) findViewById(R.id.dataNascimentoLayout);
        senhaLayout = (TextInputLayout) findViewById(R.id.senhaLayout);
        senhaConfirmacaoLayout = (TextInputLayout) findViewById(R.id.senhaConfirmacaoLayout);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        senhaConfirmacaoView.setOnEditorActionListener(editorAction);
        dataNascimentoView.addTextChangedListener(Mask.insert(Mask.MaskType.DATA, dataNascimentoView));

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Usuario usuario = new Usuario();
                usuario.setUsername(usernameView.getText().toString());
                usuario.setNome(nomeView.getText().toString());
                usuario.setSobrenome(sobrenomeView.getText().toString());
                usuario.setEmail(emailView.getText().toString());
                usuario.setDataNascimentoFormatada(dataNascimentoView.getText().toString());
                usuario.setGeneroDescricao(generoSpinner.getSelectedItem().toString());
                usuario.setSenha(senhaView.getText().toString());
                usuario.setSenhaConfirmacao(senhaConfirmacaoView.getText().toString());

                Log.d("LRDG", "usuario=" + (usuario.toString()));

                //validando dados
                if (validarFormularioUsuario(usuario)) {

                    msgErroSnackBar(coordinatorLayout, "Verifique os campos");
                    return;
                }

                CadastrarUsuarioEndpoint endpoint = RetrofitHelper.with(getApplicationContext()).createCadastrarUsuarioEndpoint();
                Call<UsuarioFormularioDTO> call = endpoint.cadastrarUsuario(
                        usuario.getUsername(),
                        usuario.getNome(),
                        usuario.getSobrenome(),
                        usuario.getEmail(),
                        usuario.getDataNascimentoFormatada(),
                        usuario.getGeneroId(),
                        usuario.getSenha(),
                        usuario.getSenhaConfirmacao()
                );

                call.enqueue(new Callback<UsuarioFormularioDTO>() {
                    @Override
                    public void onResponse(@NonNull Call<UsuarioFormularioDTO> call, @NonNull Response<UsuarioFormularioDTO> response) {
                        usuarioFormularioDTO = response.body();

                        Log.d("LRDG", "response: " + response.body());
                        Log.d("LRDG", "usuarioFormularioDTO response: " + usuarioFormularioDTO);
                        if (response.isSuccessful()) {

                            if (usuarioFormularioDTO != null) {
                                SharedPreferencesHelper sharedPreferencesHelper;
                                sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
                                sharedPreferencesHelper.setToken(usuarioFormularioDTO.getKey());
                                Log.d("LRDG", "Usuario cadastrado com sucesso!");

                                setResult(RESULT_OK, new Intent());

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } else {

                            try {
                                Gson gson = new Gson();
                                usuarioFormularioDTO = gson.fromJson(response.errorBody().string(), UsuarioFormularioDTO.class);
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
                                if (usuarioFormularioDTO.getPassword1() != null) {
                                    senhaLayout.setError(usuarioFormularioDTO.getPassword1()[0]);
                                }
                                if (usuarioFormularioDTO.getPassword2() != null) {
                                    senhaConfirmacaoLayout.setError(usuarioFormularioDTO.getPassword2()[0]);
                                }
                                if (usuarioFormularioDTO.getNon_field_errors() != null) {
                                    Log.d("LRDG", "Erro no formulário: " + usuarioFormularioDTO.getNon_field_errors()[0]);
                                }

                                //Log.d("LRDG", "errorBody toString: " + response.errorBody().toString());
                                //Log.d("LRDG", "errorBody string: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("LRDG", "Erro! Sem sucesso no cadastro! Erro: " + response.code());
                            Log.d("LRDG", "UsuarioFormularioDTO response: " + usuarioFormularioDTO);
                            Log.d("LRDG", "response toString: " + response.toString());
                            Log.d("LRDG", "response message: " + response.message());
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<UsuarioFormularioDTO> call, @NonNull Throwable t) {
                        Log.d("LRDG", "Falha ao cadastrar!");
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
            usuario.setSenha(senhaView.getText().toString());
            usuario.setSenhaConfirmacao(senhaConfirmacaoView.getText().toString());

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
        try {
            usuario.validarSenha();
            senhaLayout.setError(null);

        } catch (Exception cause) {
            senhaLayout.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarSenhaConfirmacao();
            senhaConfirmacaoLayout.setError(null);

        } catch (Exception cause) {
            senhaConfirmacaoLayout.setError(cause.getMessage());
            isOk = true;
        }

        return isOk;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Genero genero = new Genero();

        ArrayAdapter<String> adapterGenero = new ArrayAdapter<>(
                this,
                R.layout.spinner_list_style,
                genero.getGenerosDescricaoList()
        );

        adapterGenero.setDropDownViewResource(R.layout.spinner_dropdown_item);
        generoSpinner.setAdapter(adapterGenero);

        /*ArrayAdapter<Genero> adapterGenero = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Genero.values());

        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generoSpinner.setAdapter(adapterGenero);*/
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Cadastrar-se");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
