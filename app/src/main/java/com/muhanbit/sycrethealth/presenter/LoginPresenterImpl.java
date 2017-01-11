package com.muhanbit.sycrethealth.presenter;

import com.muhanbit.sycrethealth.model.LoginModel;
import com.muhanbit.sycrethealth.view.LoginView;

/**
 * Created by hwjoo on 2017-01-11.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView loginView;
    private LoginModel loginModel;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void sendLoginRequest(String userId, String password, String pin) {

    }
}
