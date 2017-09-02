package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.CadastroUsuarioDTO;
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
    private MaterialSpinner generoView;
    private TextInputLayout usernameLayout, nomeLayout, sobrenomeLayout, emailLayout, dataNascimentoLayout, senhaLayout, senhaConfirmacaoLayout;
    private CoordinatorLayout coordinatorLayout;
    private CadastroUsuarioDTO cadastroUsuarioDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Cadastrar-se");

        Button bt_cadastrar = (Button) findViewById(R.id.bt_cadastrar);

        usernameView = (TextInputEditText) findViewById(R.id.username);
        nomeView = (TextInputEditText) findViewById(R.id.nome);
        sobrenomeView = (TextInputEditText) findViewById(R.id.sobrenome);
        emailView = (TextInputEditText) findViewById(R.id.email);
        dataNascimentoView = (TextInputEditText) findViewById(R.id.dataNascimento);
        generoView = (MaterialSpinner) findViewById(R.id.genero);
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

                Log.d("LRDG", "generoView.getSelectedItem()" + (generoView.getSelectedItemId()-1));

                Usuario usuario;
                usuario = new Usuario(
                        usernameView.getText().toString(),
                        nomeView.getText().toString(),
                        sobrenomeView.getText().toString(),
                        emailView.getText().toString(),
                        dataNascimentoView.getText().toString(),
                        String.valueOf(generoView.getSelectedItemId()),
                        senhaView.getText().toString(),
                        senhaConfirmacaoView.getText().toString()
                );

                Log.d("LRDG", "usuario=" + (usuario.toString()));

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
                                    usernameLayout.setError(cadastroUsuarioDTO.getUsername()[0]);
                                }
                                if (cadastroUsuarioDTO.getFirst_name() != null) {
                                    nomeLayout.setError(cadastroUsuarioDTO.getFirst_name()[0]);
                                }
                                if (cadastroUsuarioDTO.getLast_name() != null) {
                                    sobrenomeLayout.setError(cadastroUsuarioDTO.getLast_name()[0]);
                                }
                                if (cadastroUsuarioDTO.getEmail() != null) {
                                    emailLayout.setError(cadastroUsuarioDTO.getEmail()[0]);
                                }
                                if (cadastroUsuarioDTO.getData_nascimento() != null) {
                                    dataNascimentoLayout.setError(cadastroUsuarioDTO.getData_nascimento()[0]);
                                }
                                if (cadastroUsuarioDTO.getSexo() != null) {
                                    generoView.setError(cadastroUsuarioDTO.getSexo()[0]);
                                }
                                if (cadastroUsuarioDTO.getPassword1() != null) {
                                    senhaLayout.setError(cadastroUsuarioDTO.getPassword1()[0]);
                                }
                                if (cadastroUsuarioDTO.getPassword2() != null) {
                                    senhaConfirmacaoLayout.setError(cadastroUsuarioDTO.getPassword2()[0]);
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
            usuario.setGenero(String.valueOf(generoView.getSelectedItemId()));
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
            generoView.setError(null);

        } catch (Exception cause) {
            generoView.setError(cause.getMessage());
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

        ArrayAdapter<Genero> adapterGenero = new ArrayAdapter<>(this,
                R.layout.spinner_list_style,
                Genero.values());

        adapterGenero.setDropDownViewResource(R.layout.spinner_dropdown_item);
        generoView.setAdapter(adapterGenero);

        /*ArrayAdapter<Genero> adapterGenero = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Genero.values());

        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generoView.setAdapter(adapterGenero);*/
    }
}
