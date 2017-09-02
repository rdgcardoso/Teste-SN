package br.com.teste.testerecyclerview.app.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.task.ConsultarRankingBaladasTask;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;

public class BaladaRankingFragment extends Fragment {

    private SharedPreferencesHelper sharedPreferencesHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balada_ranking, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("StartNight");

        SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swipeRefreshLayout);
        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ConsultarRankingBaladasTask(getContext(), getView(), sharedPreferencesHelper.recuperarToken()).execute();
            }
        });

        View progressBar = getActivity().findViewById(R.id.progressIndeterminateBar);
        progressBar.setVisibility(View.VISIBLE);

        new ConsultarRankingBaladasTask(getContext(), sharedPreferencesHelper.recuperarToken()).execute();

    }
}
