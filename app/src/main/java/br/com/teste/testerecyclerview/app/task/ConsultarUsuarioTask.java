package br.com.teste.testerecyclerview.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.controller.LoginActivity;
import br.com.teste.testerecyclerview.app.controller.MainActivity;
import br.com.teste.testerecyclerview.app.dto.BaladaDTO;
import br.com.teste.testerecyclerview.app.dto.UsuarioDTO;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.ws.BaladaEndpoint;
import br.com.teste.testerecyclerview.app.ws.UsuarioEndpoint;
import br.com.teste.testerecyclerview.domain.model.Balada;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.Response;

public class ConsultarUsuarioTask extends AsyncTask<Void, Void, Usuario> {

    private Context context;
    private String token;
    private Response<UsuarioDTO> response;

    public ConsultarUsuarioTask(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @Override //Pré execucao
    protected void onPreExecute() {
        Log.i("LRDG", "Pré execução ConsultarUsuarioTask");
    }

    @Override //Execução
    protected Usuario doInBackground(Void... voids) {

        Log.i("LRDG", "Execução ConsultarUsuarioTask");

        UsuarioEndpoint endpoint = RetrofitHelper.with(context).createUsuarioEndpoint();

        Usuario usuario = null;
        UsuarioDTO dto;

        try {
            Call<UsuarioDTO> call = endpoint.consultarUsuario(token);
            response = call.execute();

            if (response.isSuccessful()) {
                dto = response.body();

                if (dto != null) {
                    usuario = new Usuario(
                            dto.getId(),
                            dto.getUsername(),
                            dto.getEmail(),
                            dto.getFirst_name(),
                            dto.getLast_name(),
                            dto.getFoto()
                    );
                }
            } else {
                Log.i("LRDG", "Erro ConsultarUsuarioTask " + response.code());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    @Override
    protected void onPostExecute(Usuario usuario) {
        Log.i("LRDG", "Pós execução ConsultarUsuarioTask");

        AppCompatActivity activity = (AppCompatActivity) context;

        TextView nomeCompletoView = (TextView) activity.findViewById(R.id.nomeCompleto);
        TextView emailView = (TextView) activity.findViewById(R.id.email);
        ImageView imageView = (ImageView) activity.findViewById(R.id.profile_image);

        nomeCompletoView.setText(usuario.getNomeCompleto());
        emailView.setText(usuario.getEmail());
        Picasso.with(context).load(usuario.getFoto()).into(imageView);
    }
}
