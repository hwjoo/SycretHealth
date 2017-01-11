package com.muhanbit.sycrethealth.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.muhanbit.sycrethealth.MainActivity;
import com.muhanbit.sycrethealth.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_CONTACTS;
import static android.R.attr.button;
import static android.os.Build.VERSION_CODES.M;


public class LoginActivity extends AppCompatActivity  {

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
        Toast.makeText(LoginActivity.this,"click",Toast.LENGTH_SHORT).show();
    }

}

