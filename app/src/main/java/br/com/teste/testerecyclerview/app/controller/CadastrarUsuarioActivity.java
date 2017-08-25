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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.CadastroUsuarioDTO;
import br.com.teste.testerecyclerview.app.resources.Mask;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.CadastrarUsuarioEndpoint;
import br.com.teste.testerecyclerview.domain.model.Genero;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastrarUsuarioActivity extends StartNightActivity {

    private TextInputEditText usernameView, nomeView, sobrenomeView, emailView, dataNascimentoView, senhaView, senhaConfirmacaoView;
    private Spinner generoView;
    private TextInputLayout usernameViewContainer, nomeViewContainer, sobrenomeViewContainer, emailViewContainer, dataNascimentoViewContainer, generoViewContainer, senhaViewContainer, senhaConfirmacaoViewContainer;
    private CoordinatorLayout coordinatorLayout;
    private CadastroUsuarioDTO cadastroUsuarioDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        Button bt_cadastrar = (Button) findViewById(R.id.bt_cadastrar);

        usernameView = (TextInputEditText) findViewById(R.id.username);
        nomeView = (TextInputEditText) findViewById(R.id.nome);
        sobrenomeView = (TextInputEditText) findViewById(R.id.sobrenome);
        emailView = (TextInputEditText) findViewById(R.id.email);
        dataNascimentoView = (TextInputEditText) findViewById(R.id.dataNascimento);
        generoView = (Spinner) findViewById(R.id.genero);
        senhaView = (TextInputEditText) findViewById(R.id.senha);
        senhaConfirmacaoView = (TextInputEditText) findViewById(R.id.senhaConfirmacao);

        usernameViewContainer = (TextInputLayout) findViewById(R.id.usernameContainer);
        nomeViewContainer = (TextInputLayout) findViewById(R.id.nomeContainer);
        sobrenomeViewContainer = (TextInputLayout) findViewById(R.id.sobrenomeContainer);
        emailViewContainer = (TextInputLayout) findViewById(R.id.emailContainer);
        dataNascimentoViewContainer = (TextInputLayout) findViewById(R.id.dataNascimentoContainer);
        generoViewContainer = (TextInputLayout) findViewById(R.id.generoContainer);
        senhaViewContainer = (TextInputLayout) findViewById(R.id.senhaContainer);
        senhaConfirmacaoViewContainer = (TextInputLayout) findViewById(R.id.senhaConfirmacaoContainer);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        senhaConfirmacaoView.setOnEditorActionListener(editorAction);
        dataNascimentoView.addTextChangedListener(Mask.insert(Mask.MaskType.DATA, dataNascimentoView));

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Usuario usuario;
                usuario = new Usuario(
                        usernameView.getText().toString(),
                        nomeView.getText().toString(),
                        sobrenomeView.getText().toString(),
                        emailView.getText().toString(),
                        dataNascimentoView.getText().toString(),
                        String.valueOf(((Genero)generoView.getSelectedItem()).getIdSelecionado()),
                        senhaView.getText().toString(),
                        senhaConfirmacaoView.getText().toString()
                );

                //validando dados
                if (validarFormularioUsuario(usuario)) {

                    msgErroSnackBar(coordinatorLayout, "Verifique os campos");
                    return;
                }

                Log.d("LRDG", "usuario: " + usuario.toString());

                CadastrarUsuarioEndpoint endpoint = RetrofitHelper.with(getApplicationContext()).createCadastrarUsuarioEndpoint();
                Call<CadastroUsuarioDTO> call = endpoint.cadastrarUsuario(
                        usuario.getUsername(),
                        usuario.getNome(),
                        usuario.getSobrenome(),
                        usuario.getEmail(),
                        usuario.getDataNascimento(),
                        usuario.getGenero(),
                        usuario.getSenha(),
                        usuario.getSenhaConfirmacao()
                );

                call.enqueue(new Callback<CadastroUsuarioDTO>() {
                    @Override
                    public void onResponse(@NonNull Call<CadastroUsuarioDTO> call, @NonNull Response<CadastroUsuarioDTO> response) {
                        cadastroUsuarioDTO = response.body();

                        Log.d("LRDG", "response: " + response.body());
                        Log.d("LRDG", "cadastroUsuarioDTO response: " + cadastroUsuarioDTO);
                        if (response.isSuccessful()) {

                            if (cadastroUsuarioDTO != null) {
                                SharedPreferencesHelper sharedPreferencesHelper;
                                sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
                                sharedPreferencesHelper.setToken(cadastroUsuarioDTO.getKey());
                                Log.d("LRDG", "Usuario cadastrado com sucesso!");

                                //startActivityForResult
                                Intent it = new Intent();
                                setResult(RESULT_OK, it);

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } else {

                            try {
                                Gson gson = new Gson();
                                cadastroUsuarioDTO = gson.fromJson(response.errorBody().string(), CadastroUsuarioDTO.class);

                                Log.d("LRDG", "cadastroUsuarioDTO=" + cadastroUsuarioDTO.toString());

                                if (cadastroUsuarioDTO.getUsername() != null) {
                                    usernameViewContainer.setError(cadastroUsuarioDTO.getUsername()[0]);
                                }
                                if (cadastroUsuarioDTO.getFirst_name() != null) {
                                    nomeViewContainer.setError(cadastroUsuarioDTO.getFirst_name()[0]);
                                }
                                if (cadastroUsuarioDTO.getLast_name() != null) {
                                    sobrenomeViewContainer.setError(cadastroUsuarioDTO.getLast_name()[0]);
                                }
                                if (cadastroUsuarioDTO.getEmail() != null) {
                                    emailViewContainer.setError(cadastroUsuarioDTO.getEmail()[0]);
                                }
                                if (cadastroUsuarioDTO.getData_nascimento() != null) {
                                    dataNascimentoViewContainer.setError(cadastroUsuarioDTO.getData_nascimento()[0]);
                                }
                                if (cadastroUsuarioDTO.getSexo() != null) {
                                    generoViewContainer.setError(cadastroUsuarioDTO.getSexo()[0]);
                                }
                                if (cadastroUsuarioDTO.getPassword1() != null) {
                                    senhaViewContainer.setError(cadastroUsuarioDTO.getPassword1()[0]);
                                }
                                if (cadastroUsuarioDTO.getPassword2() != null) {
                                    senhaConfirmacaoViewContainer.setError(cadastroUsuarioDTO.getPassword2()[0]);
                                }
                                if (cadastroUsuarioDTO.getNon_field_errors() != null) {
                                    Log.d("LRDG", "Erro no formulário: " + cadastroUsuarioDTO.getNon_field_errors()[0]);
                                }

                                //Log.d("LRDG", "errorBody toString: " + response.errorBody().toString());
                                //Log.d("LRDG", "errorBody string: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("LRDG", "Erro! Sem sucesso no cadastro! Erro: " + response.code());
                            Log.d("LRDG", "CadastroUsuarioDTO response: " + cadastroUsuarioDTO);
                            Log.d("LRDG", "response toString: " + response.toString());
                            Log.d("LRDG", "response message: " + response.message());
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<CadastroUsuarioDTO> call, @NonNull Throwable t) {
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
            usuario.setDataNascimento(dataNascimentoView.getText().toString());
            usuario.setGenero(String.valueOf(((Genero)generoView.getSelectedItem()).getIdSelecionado()));
            usuario.setSenha(senhaView.getText().toString());
            usuario.setSenhaConfirmacao(senhaConfirmacaoView.getText().toString());

            return validarFormularioUsuario(usuario);
        }
    };

    private boolean validarFormularioUsuario(Usuario usuario) {

        boolean isOk = false;

        try {
            usuario.validarUsername();
            usernameViewContainer.setError("");

        } catch (Exception cause) {
            usernameViewContainer.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarNome();
            nomeViewContainer.setError("");

        } catch (Exception cause) {
            nomeViewContainer.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarSobrenome();
            sobrenomeViewContainer.setError("");

        } catch (Exception cause) {
            sobrenomeViewContainer.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarEmail();
            emailViewContainer.setError("");

        } catch (Exception cause) {
            emailViewContainer.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarDataNascimento();
            dataNascimentoViewContainer.setError("");

        } catch (Exception cause) {
            dataNascimentoViewContainer.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarGenero();
            generoViewContainer.setError("");

        } catch (Exception cause) {
            generoViewContainer.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarSenha();
            senhaViewContainer.setError("");

        } catch (Exception cause) {
            senhaViewContainer.setError(cause.getMessage());
            isOk = true;
        }
        try {
            usuario.validarSenhaConfirmacao();
            senhaConfirmacaoViewContainer.setError("");

        } catch (Exception cause) {
            senhaConfirmacaoViewContainer.setError(cause.getMessage());
            isOk = true;
        }

        return isOk;
    }

    @Override
    protected void onResume() {
        super.onResume();
        generoView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                Genero.values()));
    }
}
