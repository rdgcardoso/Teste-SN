package br.com.teste.testerecyclerview.app.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.task.ConsultarRankingBaladasTask;

public class RankingFragment extends Fragment {

    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Top Baladas");
        new ConsultarRankingBaladasTask(getContext()).execute();

        return inflater.inflate(R.layout.ranking_fragment, container, false);
    }
}
