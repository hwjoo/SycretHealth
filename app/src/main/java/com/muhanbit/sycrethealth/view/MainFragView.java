package com.muhanbit.sycrethealth.view;

import android.content.Context;

import com.muhanbit.sycrethealth.ContainerActivity;
import com.muhanbit.sycrethealth.presenter.LoginPresenter;
import com.muhanbit.sycrethealth.presenter.LoginPresenterImpl;

/**
 * Created by hwjoo on 2017-01-13.
 */

public interface MainFragView{
    void showSnackBar(String message);
    void showTimePicker();
    void showPopupMenu();
    Context getViewContext();
    void setStep(int step);
    void setTime(int totalTime, int remainTime);
    int getCurrentMinute();
    void changeBtnText(String chText);
    void showSaveDialog(String startTime, String endTime, String date);
    void progressOnOff(boolean on);
    ContainerActivity getContainer();




}
