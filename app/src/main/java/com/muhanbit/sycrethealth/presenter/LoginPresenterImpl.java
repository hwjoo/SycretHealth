package com.muhanbit.sycrethealth.presenter;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.muhanbit.sycrethealth.SycretWare;
import com.muhanbit.sycrethealth.model.LoginModel;
import com.muhanbit.sycrethealth.view.LoginView;
import com.sycretware.auth.Provider;
import com.sycretware.exception.PersonalizationDeviceUnknownException;

/**
 * Created by hwjoo on 2017-01-11.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private static final String AUTH_URL ="";
    private LoginView mLoginView;
    private LoginModel mLoginModel;

    public LoginPresenterImpl(LoginView loginView, LoginModel loginModel) {
        this.mLoginView = loginView;
        this.mLoginModel = loginModel;
    }

    @Override
    public void sendLoginRequest(final String userId, final String password, final String pin) {
        final Provider provider = SycretWare.getProvider();
        new AsyncTask<Void, Void, Void>(){
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if(provider.init(pin)) {
                        provider.secureConnect.connectEnroll(AUTH_URL, userId, password);
                    }
                } catch (PersonalizationDeviceUnknownException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();


    }
}
