package com.muhanbit.sycrethealth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    @BindView(R.id.json_text)
    TextView  jsonText;
    @BindView(R.id.encjson_text)
    TextView encJsonText;
    @BindView(R.id.json_response_text)
    TextView jsonResponseText;
    @BindView(R.id.login_next_btn)
    Button nextBtn;

    private static final int PERMISSION_REQUEST_CODE =100;

    LoginPresenter loginPresenter;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;

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

        mUserId.setText("user");
        mPassword.setText("123456");
        mPin.setText("123456");

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {

            }else {
                Log.v("TEST","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @OnClick(R.id.login_btn)
    void loginBtnClick(){
        String userId = mUserId.getText().toString();
        String password = mPassword.getText().toString();
        String pin = mPin.getText().toString();
        loginPresenter.sendPersonaRequest(userId, password, pin);

    }
    @OnClick(R.id.login_next_btn)
    void nextBtnClick(){
        Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
        startNextActivity(intent);
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

    @Override
    public void showPinErrorDialog() {


    }

    @Override
    public Context getViewContext() {
        return getBaseContext();
    }

    @Override
    public void startNextActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showInvisibleWidget(String jsonData, String encJsonData, String jsonResponseData) {
        jsonText.setVisibility(View.VISIBLE);
        encJsonText.setVisibility(View.VISIBLE);
        jsonResponseText.setVisibility(View.VISIBLE);
        nextBtn.setVisibility(View.VISIBLE);
        jsonText.setText("JSON : \n"+jsonData);
        encJsonText.setText("ENCRYPTED JSON : \n"+encJsonData);
        jsonResponseText.setText("RESPONSE JSON : \n"+jsonResponseData);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            for(int i = 0 ; i<grantResults.length; i++){
                if(grantResults[i] == -1){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                        dialogBuilder.setPositiveButton("다시하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                                alertDialog.dismiss();
                            }
                        }).setNegativeButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).setTitle("알림")
                                .setMessage("외부 데이터 접근권한이 없으면 PIN인증 할 수 없습니다.")
                                .setCancelable(false);
                        alertDialog = dialogBuilder.create();
                        alertDialog.show();

                    }else {
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                        dialogBuilder
                        .setPositiveButton("바로가기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.parse("package:" + LoginActivity.this.getPackageName()));
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                LoginActivity.this.startActivity(intent);
                                alertDialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setTitle("알림")
                        .setMessage("[설정]에서 권한을 허용해주세요.")
                        .setCancelable(false);
                        alertDialog = dialogBuilder.create();
                        alertDialog.show();

                    }
                }
            }
        }
    }

}

