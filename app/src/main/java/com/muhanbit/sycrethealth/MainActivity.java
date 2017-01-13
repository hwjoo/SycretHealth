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

import com.muhanbit.sycrethealth.fragments.OneFragment;
import com.muhanbit.sycrethealth.fragments.ThreeFragment;
import com.muhanbit.sycrethealth.fragments.TwoFragment;
import com.muhanbit.sycrethealth.view.MainView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.activity_main)
    View MainActivityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                Snackbar.make(MainActivityView,"MENU 1 Click",Snackbar.LENGTH_SHORT).show();
                break;
            case 2:
                Snackbar.make(MainActivityView, "MENU 2 Click", Snackbar.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,Menu.NONE,"ONE").setIcon(R.mipmap.ic_launcher);
        menu.add(0,2,Menu.NONE,"TWO").setIcon(R.mipmap.ic_launcher);
        return super.onCreateOptionsMenu(menu);
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(OneFragment.newInstance(),"ONE");
        adapter.addFragment(TwoFragment.newInstance(),"TWO");
        adapter.addFragment(ThreeFragment.newInstance(),"Three");

        viewPager.setAdapter(adapter);

    }
}
