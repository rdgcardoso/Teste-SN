package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.resources.Constantes;
import br.com.teste.testerecyclerview.app.task.ConsultarUsuarioTask;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;

public class UsuarioDetalhesFragment extends Fragment {

    private SharedPreferencesHelper sharedPreferencesHelper;
    private CoordinatorLayout coordinatorLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usuario_detalhes, container, false);

        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), UsuarioEditarActivity.class);
                startActivityForResult(i, Constantes.USUARIO_EDITOU_REQUEST);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());
        new ConsultarUsuarioTask(getContext(), sharedPreferencesHelper.recuperarToken()).execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == Constantes.USUARIO_EDITOU_REQUEST) {
                Snackbar.make(coordinatorLayout, "Alterações salvas com sucesso", Snackbar.LENGTH_SHORT).show();
        }
    }
}
