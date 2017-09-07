package br.com.teste.testerecyclerview.app.controller;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.app.dto.LogoutDTO;
import br.com.teste.testerecyclerview.app.task.PreencheMenuUsuarioTask;
import br.com.teste.testerecyclerview.app.util.RetrofitHelper;
import br.com.teste.testerecyclerview.app.util.SharedPreferencesHelper;
import br.com.teste.testerecyclerview.app.ws.LogoutEndpoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends StartNightActivity {

    private DrawerLayout drawerLayout;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private NavigationView navigationView;
    private LogoutDTO logoutDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("@string/app_name");
        setSupportActionBar(toolbar);

        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        new PreencheMenuUsuarioTask(this, sharedPreferencesHelper.recuperarToken()).execute();

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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
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
