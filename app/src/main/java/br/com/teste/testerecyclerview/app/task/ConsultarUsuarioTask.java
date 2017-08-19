package br.com.teste.testerecyclerview.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
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
    private ProgressDialog progressDialog;
    private AppCompatActivity activity;
    private String token;

    public ConsultarUsuarioTask(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @Override //Pré execucao
    protected void onPreExecute() {
        Log.i("LRDG", "Pré execução");

        progressDialog = new ProgressDialog(context);
        progressDialog = ProgressDialog.show(context, "Aguarde", "Carregando Usuario...", true, true);
    }

    @Override //Execução
    protected Usuario doInBackground(Void... voids) {

        Log.i("LRDG", "Execução");

        UsuarioEndpoint endpoint = RetrofitHelper.with(context).createUsuarioEndpoint();

        Usuario usuario = null;
        UsuarioDTO dto;

        try {

            Call<UsuarioDTO> call = endpoint.consultarUsuario(token);
            Response<UsuarioDTO> response = call.execute();

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
                Log.i("LRDG", "Erro");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    @Override
    protected void onPostExecute(final Usuario usuario) {
        Log.i("LRDG", "Pós execução");

        activity = (AppCompatActivity) context;
        TextView nomeCompletoView = (TextView) activity.findViewById(R.id.nomeCompleto);
        TextView emailView = (TextView) activity.findViewById(R.id.email);
        ImageView imageView = (ImageView) activity.findViewById(R.id.profile_image);

        nomeCompletoView.setText(usuario.getNomeCompleto());
        emailView.setText(usuario.getEmail());
        Picasso.with(context).load(usuario.getFoto()).into(imageView);

        progressDialog.dismiss();
    }
}
