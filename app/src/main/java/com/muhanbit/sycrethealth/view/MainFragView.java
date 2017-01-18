package com.muhanbit.sycrethealth.view;

import android.content.Context;

/**
 * Created by hwjoo on 2017-01-13.
 */

public interface MainFragView {
    void showToast(String toast);
    void showTimePicker();
    void showPopupMenu();
    Context getViewContext();
    void setStep(int step);
    void setTime(int totalTime, int remainTime);
    int getCurrentMinute();
    void changeBtnText(String chText);
    void showSaveDialog();

}
