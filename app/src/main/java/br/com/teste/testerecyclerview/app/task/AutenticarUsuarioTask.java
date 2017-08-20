package br.com.teste.testerecyclerview.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import br.com.teste.testerecyclerview.app.controller.MainActivity;
import br.com.teste.testerecyclerview.app.dto.TokenDTO;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.LoginEndpoint;
import retrofit2.Call;
import retrofit2.Response;

public class AutenticarUsuarioTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private ProgressDialog progressDialog;
    private AppCompatActivity activity;
    private String username;
    private String password;
    private TokenDTO tokenDTO;
    private SharedPreferencesHelper sharedPreferences;

    public AutenticarUsuarioTask(Context context, String username, String password) {
        this.context = context;
        this.username = username;
        this.password = password;
    }

    @Override //Pré execucao
    protected void onPreExecute() {
        Log.i("LRDG", "Pré execução AutenticarUsuarioTask");

        progressDialog = new ProgressDialog(context);
        progressDialog = ProgressDialog.show(context, "Aguarde", "Logando...", true, true);
    }

    @Override //Execução
    protected Boolean doInBackground(Void... voids) {

        Log.i("LRDG", "Execução AutenticarUsuarioTask");

        LoginEndpoint endpoint = RetrofitHelper.with(context).createLoginEndpoint();

        try {
            Call<TokenDTO> call = endpoint.logarUsuario(username, password);
            Response<TokenDTO> response = call.execute();

            if (response.isSuccessful()) {
                tokenDTO = response.body();

                if (tokenDTO != null) {
                    sharedPreferences = new SharedPreferencesHelper(context);
                    sharedPreferences.setTokenCache(tokenDTO.getKey());
                    Log.d("LRDG", "Autenticado! Token " + tokenDTO.getKey());

                    return true;
                }
            } else {
                Log.i("LRDG", "Erro ConsultarBaladaTask");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean resultado) {
        Log.i("LRDG", "Pós execução AutenticarUsuarioTask");

        if (resultado) {
            activity = (AppCompatActivity) context;
            Intent i = new Intent(context, MainActivity.class);
            activity.startActivity(i);
        } else {
            Toast.makeText(context, "Usuário e/ou Senha Inválidos!", Toast.LENGTH_SHORT).show();
        }

        progressDialog.dismiss();
    }
}
