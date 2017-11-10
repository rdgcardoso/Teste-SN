package br.com.teste.testerecyclerview.app.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balada_detalhes);

        Intent i = getIntent();
        balada = (Balada) i.getSerializableExtra("balada");

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        enviaIdBalada(balada.getId(), detalhesBaladaFragment);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setTitle(balada.getNome());

        setupCollapsingToolbar();
        setupToolbar();
    }

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(balada.getNome());
        }
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

    private void setupViewPager(ViewPager viewPager) {
        BaladaEventosFragment eventosBaladaFragment = new BaladaEventosFragment();
        detalhesBaladaFragment = new BaladaDetalhesFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(detalhesBaladaFragment, "DETALHES");
        adapter.addFrag(eventosBaladaFragment, "EVENTOS");
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

        private void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void enviaIdBalada(long id, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        fragment.setArguments(bundle);
    }
}
