package br.com.teste.testerecyclerview.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.domain.model.Balada;

public class BaladaAdapter extends RecyclerView.Adapter {

    private List<Balada> baladaList;
    private Context context;
    private CustomItemClickListener listener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_balada, parent, false);
        final BaladaViewHolder holder = new BaladaViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        BaladaViewHolder holder = (BaladaViewHolder) viewHolder;
        Balada balada = baladaList.get(position);

        holder.nomeView.setText(balada.getNome());
        holder.endereco1View.setText(balada.getEndereco1());
        holder.endereco2View.setText(balada.getEndereco2());
        holder.musicaTipoView.setText(balada.getMusicaTipo());
        holder.notaView.setText(balada.getNota());
        holder.precoView.setText(balada.getPreco());
    }

    @Override
    public int getItemCount() {
        return baladaList.size();
    }

    public BaladaAdapter(Context context, ArrayList<Balada> baladaList, CustomItemClickListener listener) {
        this.baladaList = baladaList;
        this.context = context;
        this.listener = listener;
    }
}
