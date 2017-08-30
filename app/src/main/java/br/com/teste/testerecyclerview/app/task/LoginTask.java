package br.com.teste.testerecyclerview.app.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.controller.MainActivity;
import br.com.teste.testerecyclerview.app.dto.LoginDTO;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.LoginEndpoint;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.Response;

public class LoginTask extends AsyncTask<Void, Void, LoginDTO> {

    private Context context;
    private Usuario usuario;
    private Response response;
    private LoginDTO loginDTO;
    private SharedPreferencesHelper sharedPreferences;
    private boolean autenticado;
    private TextInputLayout usernameLayout, senhaLayout;
    private CoordinatorLayout coordinatorLayout;
    private AppCompatActivity activity;
    private Button bt_entrar;
    private TextView bt_cadastrar;
    private View progressBar;



    public LoginTask(Usuario usuario, Context context) {
        this.context = context;
        this.usuario = usuario;
    }

    @Override //Pré execucao
    protected void onPreExecute() {
        Log.i("LRDG", "Pré execução LoginTask");

        activity = (AppCompatActivity) context;
        progressBar = activity.findViewById(R.id.progressIndeterminateBar);
        bt_entrar = activity.findViewById(R.id.bt_entrar);
        bt_cadastrar = activity.findViewById(R.id.cadastrar);

        progressBar.setVisibility(View.VISIBLE);
        bt_entrar.setVisibility(View.GONE);
        bt_cadastrar.setVisibility(View.GONE);

    }

    @Override //Execução
    protected LoginDTO doInBackground(Void... voids) {

        Log.i("LRDG", "Execução LoginTask");
        autenticado = false;
        LoginEndpoint endpoint = RetrofitHelper.with(context).createLoginEndpoint();


        try {
            Call<LoginDTO> call = endpoint.logarUsuario(
                    usuario.getUsername(),
                    usuario.getSenha()
            );

            response = call.execute();
            loginDTO = (LoginDTO) response.body();

            if (response.isSuccessful()) {
                if (loginDTO != null) {
                    sharedPreferences = new SharedPreferencesHelper(context);
                    sharedPreferences.setToken(loginDTO.getKey());
                    Log.d("LRDG", "Autenticado! Token =" + loginDTO.getKey());

                    autenticado = true;
                }
            } else {
                try {
                    Gson gson = new Gson();
                    loginDTO = gson.fromJson(response.errorBody().string(), LoginDTO.class);

                    Log.d("LRDG", "loginDTO=" + loginDTO.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("LRDG", "Erro! Sem sucesso no login! Erro: " + response.code());
                Log.d("LRDG", "loginDTO response: " + loginDTO);
                Log.d("LRDG", "response toString: " + response.toString());
                Log.d("LRDG", "response message: " + response.message());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return loginDTO;
    }

    @Override
    protected void onPostExecute(LoginDTO loginDTO) {
        Log.i("LRDG", "Pós execução LoginTask");

        usernameLayout = activity.findViewById(R.id.usernameLayout);
        senhaLayout = activity.findViewById(R.id.senhaLayout);
        coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);

        if (autenticado) {
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
            activity.finish();
        } else {
            if (loginDTO.getNon_field_errors() != null) {
                usernameLayout.setError(null);
                senhaLayout.setError(null);

                Snackbar snackbar = Snackbar.make(coordinatorLayout, loginDTO.getNon_field_errors()[0], Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.RED);
                snackbar.show();
                Log.d("LRDG", "Erro no formulário: " + loginDTO.getNon_field_errors()[0]);
            }
            progressBar.setVisibility(View.GONE);
            bt_entrar.setVisibility(View.VISIBLE);
            bt_cadastrar.setVisibility(View.VISIBLE);
        }
    }
}

