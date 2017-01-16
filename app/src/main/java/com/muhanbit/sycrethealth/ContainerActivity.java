package com.muhanbit.sycrethealth;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.muhanbit.sycrethealth.fragments.MainFragment;
import com.muhanbit.sycrethealth.fragments.ThreeFragment;
import com.muhanbit.sycrethealth.fragments.TwoFragment;
import com.muhanbit.sycrethealth.view.ContainerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContainerActivity extends AppCompatActivity implements ContainerView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MainFragment.newInstance(),"ONE");
        adapter.addFragment(TwoFragment.newInstance(),"TWO");
        adapter.addFragment(ThreeFragment.newInstance(),"Three");

        viewPager.setAdapter(adapter);
    }

}
