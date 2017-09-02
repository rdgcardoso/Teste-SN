package br.com.teste.testerecyclerview.app.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.UsuarioDTO;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.ws.UsuarioEndpoint;
import br.com.teste.testerecyclerview.app.resources.CodigoRetornoHTTP;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.Response;

public class PreencheMenuUsuarioTask extends AsyncTask<Void, Void, Usuario> {

    private Context context;
    private String token;
    private Response<UsuarioDTO> response;
    private int responseCode;

    public PreencheMenuUsuarioTask(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @Override //Pré execucao
    protected void onPreExecute() {
        Log.i("LRDG", "Pré execução PreencheMenuUsuarioTask");
    }

    @Override //Execução
    protected Usuario doInBackground(Void... voids) {

        Log.i("LRDG", "Execução PreencheMenuUsuarioTask");

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
                            dto.getFoto(),
                            dto.getSexo(),
                            dto.getDataNascimento()
                    );
                }
            } else {
                Log.i("LRDG", "Erro PreencheMenuUsuarioTask " + response.code());
                responseCode = response.code();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    @Override
    protected void onPostExecute(Usuario usuario) {
        Log.i("LRDG", "Pós execução PreencheMenuUsuarioTask");

        CodigoRetornoHTTP codigo = new CodigoRetornoHTTP(responseCode);
        if (codigo.notAuthorized(context)) {
            return;
        }

        AppCompatActivity activity = (AppCompatActivity) context;

        TextView nomeCompletoView = (TextView) activity.findViewById(R.id.nomeCompleto);
        TextView emailView = (TextView) activity.findViewById(R.id.email);
        ImageView imageView = (ImageView) activity.findViewById(R.id.profile_image);
        ImageView imageBlurView = (ImageView) activity.findViewById(R.id.profile_imageBlur);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) activity.findViewById(R.id.coordinatorLayout);

        nomeCompletoView.setText(usuario.getNomeCompleto());
        emailView.setText(usuario.getEmail());

        if (usuario.getFoto() != null) {
            Picasso.with(context).load(usuario.getFoto()).into(imageView);
            Picasso.with(context).load(usuario.getFoto()).into(imageBlurView);
        }

        Snackbar.make(coordinatorLayout, "Bem-vindo, " + usuario.getNome() + "!", Snackbar.LENGTH_LONG).show();
    }
}
