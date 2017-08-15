package br.com.teste.testerecyclerview.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.teste.testerecyclerview.R;

class BaladaViewHolder extends RecyclerView.ViewHolder {

    final TextView nomeView;
    final TextView endereco1View;
    final TextView endereco2View;
    final TextView musicaTipoView;
    final TextView notaView;
    final TextView precoView;
    final ImageView fotoView;

    BaladaViewHolder(final View itemView) {
        super(itemView);

        nomeView = itemView.findViewById(R.id.nome);
        endereco1View = itemView.findViewById(R.id.endereco1);
        endereco2View = itemView.findViewById(R.id.endereco2);
        musicaTipoView = itemView.findViewById(R.id.musicaTipo);
        notaView = itemView.findViewById(R.id.nota);
        precoView = itemView.findViewById(R.id.preco);
        fotoView = itemView.findViewById(R.id.foto);
    }
}
