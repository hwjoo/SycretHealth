package com.muhanbit.sycrethealth.view;

/**
 * Created by hwjoo on 2017-01-11.
 */

public interface LoginView {
    void showPersonaErrorCode(String errorCode);
    void showLoginState(String status);
    void progressOnOff(boolean on);
    void showPinErrorDialog();
}
