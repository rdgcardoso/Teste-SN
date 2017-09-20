package br.com.teste.testerecyclerview.app.task.apagar;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.UsuarioDTO;
import br.com.teste.testerecyclerview.app.resources.CodigoRetornoHTTP;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.ws.UsuarioEndpoint;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.Response;

public class ConsultarUsuarioTask extends AsyncTask<Void, Void, Usuario> {

    private Context context;
    private String token;
    private Response<UsuarioDTO> response;
    private int responseCode;
    private AppCompatActivity activity;
    private ProgressBar progressBar;

    public ConsultarUsuarioTask(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @Override //Pré execucao
    protected void onPreExecute() {
        Log.i("LRDG", "Pré execução ConsultarUsuarioTask");
        activity = (AppCompatActivity) context;
        progressBar = activity.findViewById(R.id.progressIndeterminateBar);
        progressBar.setVisibility(View.VISIBLE);
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
                            dto.getFoto(),
                            dto.getSexo(),
                            dto.getDataNascimentoFormatada()
                    );

                    Log.d("LRDG", "=usuario na consulta = " + usuario.toString());
                    Log.d("LRDG", "=usuarioDTO na consulta = " + dto.toString());

                }
            } else {
                Log.i("LRDG", "Erro ConsultarUsuarioTask " + response.code());
                responseCode = response.code();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    @Override
    protected void onPostExecute(Usuario usuario) {
        Log.i("LRDG", "Pós execução ConsultarUsuarioTask");

        progressBar.setVisibility(View.GONE);

        if (CodigoRetornoHTTP.notAuthorized(context, responseCode)) {
            return;
        }

        TextView nomeCompletoView = activity.findViewById(R.id.nomeCompleto);
        TextView idadeView = activity.findViewById(R.id.idade);
        TextView usernameView = activity.findViewById(R.id.username);
        TextView emailView = activity.findViewById(R.id.email);
        TextView generoView = activity.findViewById(R.id.genero);
        ProgressBar progressBar = activity.findViewById(R.id.progressIndeterminateBar);
        View detalhesUsuarioContainer = activity.findViewById(R.id.detalhesUsuarioContainer);


        ImageView imageView = activity.findViewById(R.id.profile_image);
        ImageView imageBlurView = activity.findViewById(R.id.profile_imageBlur);

        nomeCompletoView.setText(usuario.getNomeCompleto() + ",");
        idadeView.setText(usuario.getIdade() + " anos");
        usernameView.setText(usuario.getUsername());
        emailView.setText(usuario.getEmail());
        generoView.setText(usuario.getGeneroDescricao());

        if (usuario.getFoto() != null) {
            Picasso.with(context).load(usuario.getFoto()).into(imageView);
            Picasso.with(context).load(usuario.getFoto()).into(imageBlurView);
        }

        progressBar.setVisibility(View.GONE);
        detalhesUsuarioContainer.setVisibility(View.VISIBLE);
    }
}
