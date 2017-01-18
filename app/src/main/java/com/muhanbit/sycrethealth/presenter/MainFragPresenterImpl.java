package com.muhanbit.sycrethealth.presenter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.muhanbit.sycrethealth.PedometerService;
import com.muhanbit.sycrethealth.view.MainFragView;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

/**
 * Created by hwjoo on 2017-01-16.
 */

public class MainFragPresenterImpl implements MainFragPresenter {
    static MainFragView mMainFragView;
    private boolean startFlag;


    public MainFragPresenterImpl(MainFragView fragView) {
        this.mMainFragView = fragView;
        startFlag = true;
    }
    public static MainFragView getmMainFragView(){
        return mMainFragView;
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
        float remainTime = circleProgressView.getCurrentValue();
        circleProgressView.setTextMode(TextMode.TEXT);
        circleProgressView.setUnitVisible(false);
        if(remainTime == 0) {
            circleProgressView.setValue(0);
        }else{
            circleProgressView.setValueAnimated(remainTime, 0, 1000);
        }
        circleProgressView.setText("0시간 0분");
    }
    @Override
    public void initStep(TextView stepText) {
        stepText.setText("0 Step");
    }
    @Override
    public void setTimeForProgress(CircleProgressView circleProgressView ,int hour, int minute) {
        int totalMinute = hour*60 + minute;
        circleProgressView.setMaxValue(totalMinute);
        circleProgressView.setUnitVisible(false);
        circleProgressView.setValueAnimated(totalMinute);
        circleProgressView.setText(hour+"시간 "+minute+"분");
    }

    @Override
    public void clickResetMenu(CircleProgressView circleProgressView) {
        this.initCircleProgress(circleProgressView);
    }

    @Override
    public void clickSetMenu() {
        mMainFragView.showTimePicker();
    }

    @Override
    public boolean clickActionBtn() {

        if(mMainFragView.getCurrentMinute()==0){
            mMainFragView.showToast("시간설정 후 시작하세요.");
            return false;
        }
        if(!this.isServiceRunningCheck("com.muhanbit.sycrethealth.PedometerService")){
            startFlag = true;
        }

        Intent intent = new Intent(mMainFragView.getViewContext(), PedometerService.class);
        if(startFlag && !this.isServiceRunningCheck("com.muhanbit.sycrethealth.PedometerService")){
            Toast.makeText(mMainFragView.getViewContext(), "start", Toast.LENGTH_SHORT).show();
            intent.putExtra("total_minute",mMainFragView.getCurrentMinute());
            mMainFragView.getViewContext().startService(intent);
            mMainFragView.changeBtnText("STOP");
            startFlag=false;
        }else{
            Toast.makeText(mMainFragView.getViewContext(), "stop", Toast.LENGTH_SHORT).show();
            mMainFragView.getViewContext().stopService(intent);
            mMainFragView.changeBtnText("START");
            mMainFragView.showSaveDialog();
            startFlag = true;
        }
        return true;
    }

    /*
     * service 실행여부 판단.
     */

    @Override
    public boolean isServiceRunningCheck(String servicename) {
        ActivityManager manager = (ActivityManager) mMainFragView.getViewContext().getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (servicename.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;


    }


}
