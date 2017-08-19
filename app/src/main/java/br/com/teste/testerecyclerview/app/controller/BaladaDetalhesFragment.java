package br.com.teste.testerecyclerview.app.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

        long id = getArguments().getLong("id");
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this.getContext());
        new ConsultarBaladaTask(getContext(), id, sharedPreferencesHelper.recuperarTokenCache()).execute();

        return inflater.inflate(R.layout.detalhes_balada_fragment, container, false);
    }


}
