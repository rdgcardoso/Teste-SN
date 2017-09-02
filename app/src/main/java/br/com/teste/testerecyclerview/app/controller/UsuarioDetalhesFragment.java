package br.com.teste.testerecyclerview.app.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.task.ConsultarUsuarioTask;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;

public class UsuarioDetalhesFragment extends Fragment {

    SharedPreferencesHelper sharedPreferencesHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usuario_detalhes, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "FAB clicado!", Toast.LENGTH_LONG).show();
            }
        });

        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());

        new ConsultarUsuarioTask(getContext(), sharedPreferencesHelper.recuperarToken()).execute();

        return view;
    }

}
