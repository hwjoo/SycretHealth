package com.muhanbit.sycrethealth.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.muhanbit.sycrethealth.R;
import com.muhanbit.sycrethealth.presenter.MainFragPresenter;
import com.muhanbit.sycrethealth.presenter.MainFragPresenterImpl;
import com.muhanbit.sycrethealth.view.MainFragView;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hwjoo on 2017-01-13.
 */

public class MainFragment extends Fragment implements MainFragView,
        TimePickerDialog.OnTimeSetListener,PopupMenu.OnMenuItemClickListener{
    private static final int TIME_PICKER_HOUR = 24;
    private static final int TIME_PICKER_MIN = 0;

    @BindView(R.id.circle_progress)
    CircleProgressView mCircleProgressView;
    @BindView(R.id.main_overflow)
    ImageView mainOverflow;

    MainFragPresenter mMainFragPresenter;

    public static MainFragment newInstance(){
        /*
         * fragment 초기화, 생성자대신 newInstance의 Bundle 사용
         * 아래와 같이 setArguments로 data setting
         * 이후 onCreteView에서 getArguments로 bundle get 가능
         */
        MainFragment mainFragment = new MainFragment();
//        Bundle data  = new Bundle();
//        oneFragment.setArguments(data);
        return mainFragment;
    }
    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        mMainFragPresenter = new MainFragPresenterImpl(this);

        ButterKnife.bind(this, view);
        mMainFragPresenter.initCircleProgress(mCircleProgressView);

        return view;
    }

    @OnClick(R.id.circle_progress)
    void clickCircleProgress(){
        mMainFragPresenter.clickProgress();
    }
    @OnClick(R.id.main_overflow)
    void clickMainOverflow(){
        mMainFragPresenter.clickOverflow();
    }

    @Override
    public void showTimePicker() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this,TIME_PICKER_HOUR,TIME_PICKER_MIN,true);
        /*
         * Android M에서 getColor deprecated. 따라서 아래와 같이 M기준으로 devide
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary,getActivity().getTheme()));
        }else{
            timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        }
        timePickerDialog.show(getActivity().getFragmentManager(),"time_picker");
    }

    @Override
    public void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(),mainOverflow);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mMainFragPresenter.setTimeForProgress(mCircleProgressView, hourOfDay, minute);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_set_time :
                mMainFragPresenter.clickSetMenu();
                break;
            case R.id.action_reset_time :
                mMainFragPresenter.clickResetMenu(mCircleProgressView);
                break;
        }
        return false;
    }
}
