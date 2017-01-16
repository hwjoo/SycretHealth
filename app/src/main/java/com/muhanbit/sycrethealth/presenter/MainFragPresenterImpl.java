package com.muhanbit.sycrethealth.presenter;

import com.muhanbit.sycrethealth.fragments.MainFragment;
import com.muhanbit.sycrethealth.view.MainFragView;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

/**
 * Created by hwjoo on 2017-01-16.
 */

public class MainFragPresenterImpl implements MainFragPresenter{
    MainFragView mMainFragView;
    public MainFragPresenterImpl(MainFragView fragView) {
        this.mMainFragView = fragView;
    }

    @Override
    public void clickProgress() {
        mMainFragView.showTimePicker();
    }

    @Override
    public void clickOverflow() {
        mMainFragView.showPopupMenu();
    }

    @Override
    public void initCircleProgress(CircleProgressView circleProgressView) {
        circleProgressView.setTextMode(TextMode.TEXT);
        circleProgressView.setUnitVisible(false);
        circleProgressView.setMaxValue(100);
        circleProgressView.setValue(0);
        circleProgressView.setText("0시간 0분");
    }

    @Override
    public void setTimeForProgress(CircleProgressView circleProgressView ,int hour, int minute) {
        int totalMinute = hour*60 + minute;
        circleProgressView.setMaxValue(totalMinute);
        circleProgressView.setUnitVisible(false);
        circleProgressView.setValue(totalMinute);
        circleProgressView.setText(hour+"시간 "+minute+"분");
    }

    @Override
    public void clickResetMenu(CircleProgressView circleProgressView) {
        this.setTimeForProgress(circleProgressView, 0,0);
    }

    @Override
    public void clickSetMenu() {
        mMainFragView.showTimePicker();
    }


}
