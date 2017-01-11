package com.muhanbit.sycrethealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.muhanbit.sycrethealth.model.LoginModel;
import com.muhanbit.sycrethealth.model.LoginModelImpl;
import com.muhanbit.sycrethealth.presenter.LoginPresenter;
import com.muhanbit.sycrethealth.presenter.LoginPresenterImpl;
import com.muhanbit.sycrethealth.view.LoginView;
import com.sycretware.auth.Environment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.user_id) EditText mUserId;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.pin_number) EditText mPin;
    @BindView(R.id.login_btn) Button mLoginBtn;

    LoginPresenter loginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        LoginModel loginModel = new LoginModelImpl();
        loginPresenter = new LoginPresenterImpl(this, loginModel);




    }

    @OnClick(R.id.login_btn) void loginBtnClick(){
        if(Environment.initialize(this)) {
            String userId = mUserId.getText().toString();
            String password = mPassword.getText().toString();
            String pin = mPin.getText().toString();
            loginPresenter.sendLoginRequest(userId, password, pin);

        }
    }

}

