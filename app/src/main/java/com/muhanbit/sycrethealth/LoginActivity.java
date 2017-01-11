package com.muhanbit.sycrethealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.muhanbit.sycrethealth.view.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.user_id) EditText mUserId;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.pin_number) EditText mPin;
    @BindView(R.id.login_btn) Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.login_btn) void loginBtnClick(){
        Toast.makeText(LoginActivity.this, "click", Toast.LENGTH_SHORT).show();
    }

}

