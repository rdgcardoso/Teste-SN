package br.com.teste.testerecyclerview.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesHelper {

    private Context context;
    private final static String meutoken = "meutoken1111111111111";
    private SharedPreferences sharedPreferences;
    private String token;


    public SharedPreferencesHelper(Context context) {
        this.context = context;
    }

    public String recuperarTokenCache() {
        sharedPreferences = context.getSharedPreferences(meutoken, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(meutoken, "");
        Log.d("LRDG", "TOKEN ARMAZENADO: " + token);

        if (!token.equals("")) {
            token = "Token " + token;
        }

        return token;
    }

    public void salvarTokenCache(String key) {
        sharedPreferences = context.getSharedPreferences(meutoken, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(meutoken, key);
        editor.apply();
    }

    public void setToken(String token) {
        this.token = token;
    }
}
