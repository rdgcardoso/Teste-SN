package br.com.teste.testerecyclerview.app.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.task.ConsultarBaladaTask;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;

public class BaladaDetalhesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balada_detalhes, container, false);

        long id = getArguments().getLong("id");
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this.getContext());

        View progressBar = view.findViewById(R.id.progressIndeterminateBar);
        View detalhesBaladaContainer = view.findViewById(R.id.detalhesBaladaContainer);
        progressBar.setVisibility(View.VISIBLE);
        detalhesBaladaContainer.setVisibility(View.GONE);

        new ConsultarBaladaTask(getContext(), id, sharedPreferencesHelper.recuperarToken()).execute();

        return view;
    }
}
