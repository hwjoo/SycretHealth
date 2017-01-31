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
    Context getViewContext();
    void startNextActivity(Intent intent);
    void showInvisibleWidget(String jsonData, String encJsonData, String jsonResponseData);
}
