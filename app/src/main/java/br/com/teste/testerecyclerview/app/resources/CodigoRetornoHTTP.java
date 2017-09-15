package br.com.teste.testerecyclerview.app.resources;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.teste.testerecyclerview.app.controller.LoginActivity;

public abstract class CodigoRetornoHTTP {

    public static boolean notAuthorized(Context context, int code) {
        if (code == 401) {
            Log.i("LRDG", "Erro, não autorizado! Cod: " + code);
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("sessaoExpirou", true);
            context.startActivity(intent);
            return true;
        }  else {
            return false;
        }
    }
}
