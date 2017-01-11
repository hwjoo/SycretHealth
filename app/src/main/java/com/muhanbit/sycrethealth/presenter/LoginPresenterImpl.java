package com.muhanbit.sycrethealth.presenter;

import com.muhanbit.sycrethealth.model.LoginModel;
import com.muhanbit.sycrethealth.view.LoginView;

/**
 * Created by hwjoo on 2017-01-11.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView mLoginView;
    private LoginModel mLoginModel;

    public LoginPresenterImpl(LoginView loginView, LoginModel loginModel) {
        this.mLoginView = loginView;
        this.mLoginModel = loginModel;
    }

    @Override
    public void sendLoginRequest(String userId, String password, String pin) {

    }
}
