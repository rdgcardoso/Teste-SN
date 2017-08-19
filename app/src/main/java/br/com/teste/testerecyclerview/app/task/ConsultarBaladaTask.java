package br.com.teste.testerecyclerview.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.BaladaDTO;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.BaladaEndpoint;
import br.com.teste.testerecyclerview.domain.model.Balada;
import retrofit2.Call;
import retrofit2.Response;

public class ConsultarBaladaTask extends AsyncTask<Void, Void, Balada> {

    private Context context;
    private long id;
    private ProgressDialog progressDialog;
    private AppCompatActivity activity;
    private String token;

    public ConsultarBaladaTask(Context context, long id, String token) {
        this.context = context;
        this.id = id;
        this.token = token;
    }

    @Override //Pré execucao
    protected void onPreExecute() {
        Log.i("LRDG", "Pré execução");

        progressDialog = new ProgressDialog(context);
        progressDialog = ProgressDialog.show(context, "Aguarde", "Carregando Balada...", true, true);
    }

    @Override //Execução
    protected Balada doInBackground(Void... voids) {

        Log.i("LRDG", "Execução");

        BaladaEndpoint endpoint = RetrofitHelper.with(context).createBaladaEndpoint();

        Balada balada = null;
        BaladaDTO dto;

        try {
            Call<BaladaDTO> call = endpoint.consultarBalada(id, token);
            Response<BaladaDTO> response = call.execute();

            if (response.isSuccessful()) {
                dto = response.body();

                if (dto != null) {
                    balada = new Balada(
                            id,
                            dto.getTipo_musicas(),
                            dto.getNome(),
                            dto.getDescricao(),
                            dto.getCep(),
                            dto.getEstado(),
                            dto.getCidade(),
                            dto.getBairro(),
                            dto.getLogradouro(),
                            dto.getNumero(),
                            dto.getComplemento(),
                            dto.getAvaliacao(),
                            dto.getSite(),
                            dto.getPreco_medio(),
                            dto.isAtivo(),
                            dto.getFoto()
                    );
                }
            } else {
                Log.i("LRDG", "Erro");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return balada;
    }

    @Override
    protected void onPostExecute(final Balada balada) {
        Log.i("LRDG", "Pós execução");

        activity = (AppCompatActivity) context;
        ImageView fotoView = (ImageView) activity.findViewById(R.id.foto);
        TextView endereco1View = (TextView) activity.findViewById(R.id.endereco1);
        TextView endereco2View = (TextView) activity.findViewById(R.id.endereco2);
        TextView descricaoView = (TextView) activity.findViewById(R.id.descricao);
        TextView siteView = (TextView) activity.findViewById(R.id.site);
        TextView musicaTipoView = (TextView) activity.findViewById(R.id.tipoMusica);
        TextView precoMedioView = (TextView) activity.findViewById(R.id.precoMedio);

        Picasso.with(context).load(balada.getFoto()).into(fotoView);
        endereco1View.setText(balada.getEndereco1());
        endereco2View.setText(balada.getEndereco2());
        descricaoView.setText(balada.getDescricao());
        siteView.setText(balada.getSite());
        musicaTipoView.setText(balada.getTipoMusicas());
        precoMedioView.setText(balada.getPrecoMedio());

        progressDialog.dismiss();
    }
}
