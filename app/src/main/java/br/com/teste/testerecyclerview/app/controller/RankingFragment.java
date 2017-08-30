package br.com.teste.testerecyclerview.app.controller;

import android.graphics.Color;
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

public class RankingFragment extends Fragment {

    private SharedPreferencesHelper sharedPreferencesHelper;
    private View progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.ranking_fragment, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("StartNight");

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ConsultarRankingBaladasTask(getContext(), view, sharedPreferencesHelper.recuperarToken()).execute();
            }
        });

        progressBar = view.findViewById(R.id.progressIndeterminateBar);
        progressBar.setVisibility(View.VISIBLE);

        new ConsultarRankingBaladasTask(getContext(), sharedPreferencesHelper.recuperarToken()).execute();
        return view;
    }
}
