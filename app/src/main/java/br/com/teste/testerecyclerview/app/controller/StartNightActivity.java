package br.com.teste.testerecyclerview.app.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public abstract class StartNightActivity extends AppCompatActivity {
    static final int USUARIO_CADASTROU_REQUEST = 1;

    protected void msgErroSnackBar(CoordinatorLayout coordinatorLayout, String mensagem) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, mensagem, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }
}
