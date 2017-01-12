package com.muhanbit.sycrethealth;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.muhanbit.sycrethealth.model.LoginModel;
import com.muhanbit.sycrethealth.model.LoginModelImpl;
import com.muhanbit.sycrethealth.presenter.LoginPresenter;
import com.muhanbit.sycrethealth.presenter.LoginPresenterImpl;
import com.muhanbit.sycrethealth.view.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.user_id) EditText mUserId;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.pin_number) EditText mPin;
    @BindView(R.id.login_btn) Button mLoginBtn;

    LoginPresenter loginPresenter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if(!SycretWare.isInit()){
            SycretWare.init(getBaseContext());
        }
        LoginModel loginModel = new LoginModelImpl(getBaseContext());
        loginPresenter = new LoginPresenterImpl(this, loginModel);

    }

    @OnClick(R.id.login_btn)
    void loginBtnClick(){
        String userId = mUserId.getText().toString();
        String password = mPassword.getText().toString();
        String pin = mPin.getText().toString();
        loginPresenter.sendPersonaRequest(userId, password, pin);

    }

    @Override
    public void showPersonaErrorCode(String errorCode) {
        Toast.makeText(LoginActivity.this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginState(String status) {
        Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void progressOnOff(boolean on) {

        if(on){
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려주세요");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }
}

