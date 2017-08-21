package br.com.teste.testerecyclerview.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesHelper {

    private Context context;
    private final static String meutoken = "meutoken22222";
    private SharedPreferences sharedPreferences;
    private String token;

    public SharedPreferencesHelper(Context context) {
        this.context = context;
    }

    public String recuperarToken() {
        sharedPreferences = context.getSharedPreferences(meutoken, Context.MODE_PRIVATE);
        token = "Token " + sharedPreferences.getString(meutoken, "");
        Log.d("LRDG", "TOKEN: " + token);
        return token;
    }

    public String getToken() {
        sharedPreferences = context.getSharedPreferences(meutoken, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(meutoken, "");
        Log.d("LRDG", "TOKEN: " + token);
        return token;
    }

    public void setToken(String key) {
        sharedPreferences = context.getSharedPreferences(meutoken, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(meutoken, key);
        editor.apply();
    }

}
