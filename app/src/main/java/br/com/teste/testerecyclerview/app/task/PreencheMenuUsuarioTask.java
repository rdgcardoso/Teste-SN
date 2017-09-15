package br.com.teste.testerecyclerview.app.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
                Log.i("LRDG", "USUARIO DTO no MENU = " + dto);

                if (dto != null) {
                    usuario = new Usuario();

                    usuario.setId(dto.getId());
                    usuario.setUsername(dto.getUsername());
                    usuario.setEmail(dto.getEmail());
                    usuario.setNome(dto.getFirst_name());
                    usuario.setSobrenome(dto.getLast_name());
                    usuario.setFoto(dto.getFoto());
                    usuario.setGeneroId(dto.getSexo());
                    usuario.setDataNascimentoFormatada(dto.getDataNascimentoFormatada());
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

        if (CodigoRetornoHTTP.notAuthorized(context, responseCode)) {
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
        Log.d("LRDG", "USUARIO NO MENU = " + usuario.toString());

    }
}
