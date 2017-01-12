package com.muhanbit.sycrethealth.presenter;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhanbit.sycrethealth.SycretWare;
import com.muhanbit.sycrethealth.json.EncRequest;
import com.muhanbit.sycrethealth.json.LoginRequest;
import com.muhanbit.sycrethealth.json.LoginResponse;
import com.muhanbit.sycrethealth.model.LoginModel;
import com.muhanbit.sycrethealth.restful.ApiClient;
import com.muhanbit.sycrethealth.restful.ApiInterface;
import com.muhanbit.sycrethealth.view.LoginView;
import com.sycretware.auth.KeyStore;
import com.sycretware.auth.Provider;
import com.sycretware.exception.PersonalizationDeviceUnknownException;
import com.sycretware.obj.ExportKey;
import com.sycretware.security.Encrypt;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void sendPersonaRequest(final String userId, final String password, final String pin) {


        new AsyncTask<Void, Void, Boolean>(){
            ProgressDialog progressDialog;
            String errorCode="";
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            /*
             * doInBackground의 return이 true이면 onPostExcute()에서 아이디,패스워드 trkey로 암호화 후 server로 전송
             * return이 false이면  LoginView의 showLoginErrorCode()에서 toast로 실패알림.
             * true = mSD에 traffic key 존재, false = mSD init faile, Traffic key 획득실패 from server
             */
            @Override
            protected Boolean doInBackground(Void... params) {
                Provider provider =  SycretWare.getProvider();
                try {
                    if(provider.init(pin)) {
                        ExportKey trKey = provider.keyStore.getSecretKey(KeyStore.KEY_TRAFFIC);
                        if(trKey != null){
                            if(provider.secureConnect.connectEnroll(AUTH_URL, userId, password)){
                                return true;
                            }else{
                                errorCode = "SecureConnect Fail";
                                return false;
                            }
                        }else{
                            return true;
                        }
                    }else{
                        errorCode ="Provider init fail";
                        return false;
                    }
                } catch (PersonalizationDeviceUnknownException e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if(result){
                    sendLoginRequest(userId,password);
                }else{
                    mLoginView.showLoginErrorCode(errorCode);
                }
            }
        }.execute();
    }
    /*
     * JSON encryption.
     * ex) 실제 json : {"userid":"foo", "password":"1234" } -> encryption
     *  -> ecryption json : { "request" : "encrypted json string value" }
     */
    @Override
    public void sendLoginRequest(String userId, String password) {
        LoginRequest loginRequest = new LoginRequest(userId,password);
        ObjectMapper mapper = new ObjectMapper();
        try {
            ExportKey trKey = SycretWare.getEncryptionKey(SycretWare.TRAFFIC_KEY);
            String jsonString = mapper.writeValueAsString(loginRequest);
            String encJsonString =SycretWare.getProvider().encrypt.encryptBase64(
                    Encrypt.ENCRYPT,jsonString,"UTF-8",trKey);
            EncRequest encRequest = new EncRequest(encJsonString);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<LoginResponse> call = apiService.requestLogin(encRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    //response.body => LoginResponse
                }
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
