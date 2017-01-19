package com.muhanbit.sycrethealth.presenter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.muhanbit.sycrethealth.PedometerService;
import com.muhanbit.sycrethealth.Record;
import com.muhanbit.sycrethealth.model.MainFragModel;
import com.muhanbit.sycrethealth.view.MainFragView;

import java.util.ArrayList;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

import static android.R.attr.id;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

/**
 * Created by hwjoo on 2017-01-16.
 */

public class MainFragPresenterImpl implements MainFragPresenter {
    public static MainFragView mMainFragView;
    public MainFragModel mMainFragModel;
    private boolean startFlag;


    public MainFragPresenterImpl(MainFragView fragView, MainFragModel mainFragModel) {
        this.mMainFragView = fragView;
        this.mMainFragModel = mainFragModel;
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
//            mMainFragView.changeBtnText("START");
//            mMainFragView.showSaveDialog();
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

    @Override
    public boolean insertStepInfo(final String step, final String startTime, final String endTime, final String date) {
        final Record saveRecord = new Record(step,startTime,endTime,date);
        new AsyncTask<Void,Void,Long>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mMainFragView.progressOnOff(true);
            }

            @Override
            protected Long doInBackground(Void... voids) {
                return mMainFragModel.insertData(saveRecord);
            }

            @Override
            protected void onPostExecute(Long result) {
                super.onPostExecute(result);
                /*
                 * Sqlite insert 실패 -> -1 반환, 성공 -> 1 이상 반환(0 미포함)
                 * 따라서 반환 id가 -1보다 크면 성공
                 * sqlite에 저장완료되면 server로 date 전송
                 */
                if(result >-1){
                    mMainFragView.showToast("DATA 저장 완료");
                    sendStepRequest();
                }else{
                    mMainFragView.showToast("DATA 저장 error");
                    mMainFragView.progressOnOff(false);
                }

            }
        }.execute();


        return id >-1;
    }
    @Override
    public boolean sendStepRequest() {
        mMainFragView.progressOnOff(false);
        return false;
    }


}
