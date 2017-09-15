package br.com.teste.testerecyclerview.app.ws;


import android.support.v4.media.AudioAttributesCompat;
import android.util.TypedValue;

import org.json.JSONObject;
import org.json.JSONStringer;

import br.com.teste.testerecyclerview.app.dto.UsuarioDTO;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface UsuarioEditarEndpoint {

    @PUT("user/")
    Call<UsuarioDTO> editarUsuario(
            @Header("Authorization") String authorization,
            @Body RequestBody json);
}
