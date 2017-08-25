package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.teste.testerecyclerview.R;
import br.com.teste.testerecyclerview.domain.model.Balada;

public class BaladaDetalhesActivity extends StartNightActivity {

    private Balada balada;
    private TabLayout tabLayout;

    private BaladaDetalhesFragment detalhesBaladaFragment;

    private int[] tabIcons = {
            android.R.drawable.ic_menu_info_details,
            android.R.drawable.ic_menu_agenda,
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_balada);

        Intent i = getIntent();
        balada = (Balada) i.getSerializableExtra("balada");

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        enviaIdBalada(balada.getId(), detalhesBaladaFragment);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(balada.getNome());

    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageView fotoView = (ImageView) findViewById(R.id.foto);
        TextView endereco1View = (TextView) findViewById(R.id.endereco1);
        TextView endereco2View = (TextView) findViewById(R.id.endereco2);
        RatingBar avaliacaoView = (RatingBar) findViewById(R.id.avaliacaoEstrelas);

        Picasso.with(this).load(balada.getFoto()).into(fotoView);
        endereco1View.setText(balada.getEndereco1());
        endereco2View.setText(balada.getEndereco2());
        avaliacaoView.setRating(balada.getAvaliacao());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupTabIcons() {
        TextView tabUm = (TextView) LayoutInflater.from(this).inflate(R.layout.detalhes_balada_tab, null);
        tabUm.setText("Informações");
        tabUm.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[0], 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabUm);

        TextView tabDois = (TextView) LayoutInflater.from(this).inflate(R.layout.detalhes_balada_tab, null);
        tabDois.setText("Eventos");
        tabDois.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[1], 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabDois);
    }

    private void setupViewPager(ViewPager viewPager) {

        detalhesBaladaFragment = new BaladaDetalhesFragment();
        BaladaEventosFragment eventosBaladaFragment = new BaladaEventosFragment();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(detalhesBaladaFragment, "Detalhes");
        adapter.addFrag(eventosBaladaFragment, "Eventos");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
            //return mFragmentTitleList.get(position);
        }
    }

    public void enviaIdBalada(long id, Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        fragment.setArguments(bundle);
    }
}
