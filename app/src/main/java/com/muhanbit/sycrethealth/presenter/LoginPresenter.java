package com.muhanbit.sycrethealth.presenter;


import com.muhanbit.sycrethealth.json.LoginRequest;

/**
 * Created by hwjoo on 2017-01-11.
 */

public interface LoginPresenter {
    void sendPersonaRequest(String userId, String password, String pin);
    void sendLoginRequest(String userId, String password);

}
