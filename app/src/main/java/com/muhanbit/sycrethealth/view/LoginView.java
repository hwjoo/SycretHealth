package com.muhanbit.sycrethealth.view;

import android.content.Context;
import android.content.Intent;

/**
 * Created by hwjoo on 2017-01-11.
 */

public interface LoginView {
    void showPersonaErrorCode(String errorCode);
    void showLoginState(String status);
    void progressOnOff(boolean on);
    void showPinErrorDialog();
    Context getViewContext();
    void startNextActivity(Intent intent);
}
