package com.muhanbit.sycrethealth;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.muhanbit.sycrethealth.fragments.MainFragment;
import com.muhanbit.sycrethealth.fragments.LogFragment;
import com.muhanbit.sycrethealth.fragments.RecordFragment;
import com.muhanbit.sycrethealth.view.ContainerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContainerActivity extends AppCompatActivity implements ContainerView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    CustomViewPager viewPager;

    ViewPagerAdapter mAdapter;


    public OnInsertListener mOnInsertListener;
    public interface OnInsertListener{
        void onInserted();
    }
    public void setOnInsertListener(OnInsertListener listener){
        mOnInsertListener = listener;
    }
    public void insertedRecord(){
        if(mOnInsertListener !=null){
            mOnInsertListener.onInserted();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position!=2){
                    appBarLayout.setExpanded(true,true);
                }else{
                    appBarLayout.setExpanded(false,true);
                    LogFragment logFragment = (LogFragment) mAdapter.getItem(position);
                    logFragment.refreshLogText();
                }



            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void setupViewPager(ViewPager viewPager) {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(MainFragment.newInstance(),"메인");
        mAdapter.addFragment(RecordFragment.newInstance(),"기록");
        mAdapter.addFragment(LogFragment.newInstance(),"Log");
        viewPager.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_pin_change:
                Intent intent = new Intent(ContainerActivity.this, PinChangeActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
