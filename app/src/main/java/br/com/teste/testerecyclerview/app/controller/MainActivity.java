package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.LogoutDTO;
import br.com.teste.testerecyclerview.app.dto.UsuarioDTO;
import br.com.teste.testerecyclerview.app.resources.CodigoRetornoHTTP;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.LogoutEndpoint;
import br.com.teste.testerecyclerview.app.ws.UsuarioEndpoint;
import br.com.teste.testerecyclerview.domain.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends StartNightActivity {

    private DrawerLayout drawerLayout;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private NavigationView navigationView;
    private LogoutDTO logoutDTO;
    private TextView nomeCompletoView, emailView;
    private ImageView imageView, imageBlurView;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);

        nomeCompletoView = header.findViewById(R.id.nomeCompleto);
        imageView = header.findViewById(R.id.profile_image);
        imageBlurView = header.findViewById(R.id.profile_imageBlur);
        emailView = header.findViewById(R.id.email);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        preencheMenuUsuario();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("@string/app_name");
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked()) {
                    drawerLayout.closeDrawers();
                } else {
                    selecionarOpcaoMenu(item);
                }

                return true;
            }
        });

        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openDrawer,
                R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if(savedInstanceState == null){
            selecionarOpcaoMenu(navigationView.getMenu().findItem(R.id.ranking));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void selecionarOpcaoMenu(MenuItem item) {

        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }

        drawerLayout.closeDrawers();

        Fragment fragment;
        FragmentTransaction fragmentTransaction;

        switch (item.getItemId()) {

            case R.id.ranking:
                fragment = new BaladaRankingFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.perfil:
                fragment = new UsuarioDetalhesFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack("meu_fragment");
                fragmentTransaction.commit();
                break;

            case R.id.logout:
                logout();
                break;

            default:
                Toast.makeText(getApplicationContext(),"Opsss... Erro!",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void preencheMenuUsuario() {

        UsuarioEndpoint endpoint = RetrofitHelper.with(this).createUsuarioEndpoint();

        Log.d("LRDG", "token agora: " + sharedPreferencesHelper.recuperarToken());
        Call<UsuarioDTO> call = endpoint.consultarUsuario(sharedPreferencesHelper.recuperarToken());

        call.enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(@NonNull Call<UsuarioDTO> call, @NonNull Response<UsuarioDTO> response) {

                UsuarioDTO usuarioDTO = response.body();
                if (response.isSuccessful()) {
                    if (usuarioDTO != null) {
                        Usuario usuario = new Usuario();

                        usuario.setId(usuarioDTO.getId());
                        usuario.setUsername(usuarioDTO.getUsername());
                        usuario.setEmail(usuarioDTO.getEmail());
                        usuario.setNome(usuarioDTO.getFirst_name());
                        usuario.setSobrenome(usuarioDTO.getLast_name());
                        usuario.setFoto(usuarioDTO.getFoto());
                        usuario.setGeneroId(usuarioDTO.getSexo());
                        usuario.setDataNascimentoFormatada(usuarioDTO.getDataNascimentoFormatada());

                        nomeCompletoView.setText(usuario.getNomeCompleto());
                        emailView.setText(usuario.getEmail());

                        if (usuario.getFoto() != null) {
                            Picasso.with(getApplicationContext()).load(usuario.getFoto()).into(imageView);
                            Picasso.with(getApplicationContext()).load(usuario.getFoto()).into(imageBlurView);
                        }

                        Snackbar.make(coordinatorLayout, "Bem-vindo, " + usuario.getNome() + "!", Snackbar.LENGTH_LONG).show();
                        Log.d("LRDG", "USUARIO NO MENU = " + usuario.toString());
                    }
                } else {
                    Log.i("LRDG", "Erro preencheMenuUsuario " + response.code());
                    CodigoRetornoHTTP.notAuthorized(getApplicationContext(), response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UsuarioDTO> call, @NonNull Throwable t) {
                Log.d("LRDG", "Falha no preencheMenuUsuario!");
            }
        });
    }

    private void logout() {
        LogoutEndpoint endpoint = RetrofitHelper.with(this).createLogoutEndpoint();

        Log.d("LRDG", "token agora: " + sharedPreferencesHelper.recuperarToken());
        Call<LogoutDTO> call = endpoint.logoutUsuario(sharedPreferencesHelper.recuperarToken());

        call.enqueue(new Callback<LogoutDTO>() {
            @Override
            public void onResponse(@NonNull Call<LogoutDTO> call, @NonNull Response<LogoutDTO> response) {

                logoutDTO = response.body();
                if (response.isSuccessful()) {
                    if (logoutDTO != null) {
                        Log.d("LRDG", "Saindo! Mensagem: " + logoutDTO.getSuccess());
                        sharedPreferencesHelper.setToken("");
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.putExtra("logout", true);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Log.d("LRDG", "Erro! Sem sucesso no logout!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LogoutDTO> call, @NonNull Throwable t) {
                Log.d("LRDG", "Falha no logout!");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            navigationView.getMenu().findItem(R.id.ranking).setChecked(true);
            super.onBackPressed();
        }
    }
}
