package br.com.teste.testerecyclerview.app.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.teste.testerecyclerview.app.dto.BaladaDTO;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.ws.BaladaEndpoint;
import br.com.teste.testerecyclerview.domain.model.Balada;
import retrofit2.Call;
import retrofit2.Response;

public class ConsultaBaladasTask extends AsyncTask {

    private Context context;
    private String id;
    private BaladaEndpoint endpoint;

    public ConsultaBaladasTask(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    @Override //Pré execucao
    protected void onPreExecute() {
        Log.i("LRDG", "Pré execução");
    }

    @Override //Execução
    protected Object doInBackground(Object[] objects) {
        Log.i("LRDG", "Execução");

        endpoint = RetrofitHelper.with(context).createBaladaEndpoint();

        List<BaladaDTO> dtoList = new ArrayList<>();
        BaladaDTO dto = new BaladaDTO();

        try {
            Call<List<BaladaDTO>> call = endpoint.consultarBaladas(id);
            Response<List<BaladaDTO>> response = call.execute();

            if (response.isSuccessful()) {
                dtoList = response.body();

                List<Balada> baladaList = new ArrayList<>();
                Balada balada;

                for (BaladaDTO aux : dtoList) {
                    balada = new Balada(
                            aux.getId(),
                            aux.getNome(),
                            aux.getDescricao(),
                            aux.getCep(),
                            aux.getEstado(),
                            aux.getCidade(),
                            aux.getBairro(),
                            aux.getLogradouro(),
                            aux.getNumero(),
                            aux.getComplemento(),
                            aux.getAvaliacao(),
                            aux.getSite(),
                            aux.getPreco_medio(),
                            aux.isAtivo()
                    );
                    baladaList.add(balada);
                    //Log.i("LRDG", aux.toString());
                }
                Log.i("LRDG", baladaList.toString());
                //Log.i("LRDG", response.body().toString());
            } else {
                Log.i("LRDG", "Erro");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.i("LRDG", "Pós execução");
    }
}
