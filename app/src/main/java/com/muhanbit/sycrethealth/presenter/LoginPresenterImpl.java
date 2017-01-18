package com.muhanbit.sycrethealth.presenter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

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
import com.sycretware.crypto.Hash;
import com.sycretware.exception.PersonalizationDeviceUnknownException;
import com.sycretware.obj.ExportKey;
import com.muhanbit.sycrethealth.Encrypt;

import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hwjoo on 2017-01-11.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private static final String PERSONA_URL ="http://192.168.1.108:8080/healthcare";
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
                mLoginView.progressOnOff(true);
            }
            /*
             * doInBackground에서 persona 진행(traffic key 발급 및 단말등록)
             * doInBackground의 return이 true이면 onPostExcute()에서 아이디,패스워드 trkey로 암호화 후 server로 전송->Login, 사용자 정보확인
             * return이 false이면  LoginView의 showLoginErrorCode()에서 toast로 실패알림.
             * true = mSD에 traffic key 존재, false = mSD init faile, Traffic key 획득실패 from server
             */
            @Override
            protected Boolean doInBackground(Void... params) {
                Provider provider =  SycretWare.getProvider();
                try {
                    if(provider.init(pin)) {
                        ExportKey trKey = provider.keyStore.getSecretKey(KeyStore.KEY_TRAFFIC);
//                        if(trKey == null){
                        if(true){
                            if(provider.secureConnect.connectEnroll(PERSONA_URL, userId, password)){
                                return true;
                            }else{
                                errorCode = "SecureConnect Fail";
                                mLoginView.progressOnOff(false);
                                return false;
                            }
                        }else{
                            return true;
                        }
                    }else{
                        errorCode ="Provider init fail";
                        mLoginView.progressOnOff(false);
                        return false;
                    }
                } catch (PersonalizationDeviceUnknownException e) {
                    e.printStackTrace();
                }
                mLoginView.progressOnOff(false);

                return false;
            }
            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if(result){
                    sendLoginRequest(userId,password);
                }else{
                    mLoginView.showPersonaErrorCode(errorCode);
                }
            }
        }.execute();
    }
    /*
     * JSON encryption.
     * ex) 실제 json : {"userid":"foo", "password":"1234" } -> encryption
     *  -> ecryption json : { "request" : "encrypted json string value" }
     *
     *  실제 상용서비스에서는 traffic key를 사용해 서버 통신 암복호화진행,
     *  but, 현재 기능구현 부족으로, 내부 Encrypt class를 통해 password hash값으로
     *  암복호화 진행
     */
    @Override
    public void sendLoginRequest(String userId, String password) {

        try {
//            ExportKey trKey = SycretWare.getEncryptionKey(SycretWare.TRAFFIC_KEY);
            String pwHash = Hash.HashString((String)null,password);
            LoginRequest loginRequest = new LoginRequest(userId,pwHash);
            ObjectMapper mapper = new ObjectMapper();

            String jsonString = mapper.writeValueAsString(loginRequest);
//            String encJsonString =SycretWare.getProvider().encrypt.encryptBase64(
//                    Encrypt.ENCRYPT,jsonString,"UTF-8",trKey);
            String tempTrKey = Hash.HashString((String)null,SycretWare.getDeviceIdBas64Encoded());
            String encJsonString = Encrypt.encrypt(true, jsonString,tempTrKey);

            EncRequest encRequest = new EncRequest(encJsonString);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<LoginResponse> call = apiService.requestLogin(SycretWare.getDeviceIdBas64Encoded(), encRequest);
            Log.d("TEST", String.valueOf(call.request().url()));
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    /*
                     * responseMsgCd :
                     * USER_NOTFOUND (사용자존재 하지않을경우)
	                 * USER_AUTH_FAIL (사용자 인증 실패)
	                 * SYSTEM_ERROR (시스템 에러 Exception )
                     */
                    LoginResponse loginResponse = response.body();
                    String responseState= loginResponse.getResponse();
                    String responseMsgCd= loginResponse.getResponseMsgCd();
                    mLoginView.progressOnOff(false);
                    if(response.body().getResponse().equals("SUCCESS")){
                        mLoginView.showLoginState("Login success");
                    }else if(response.body().getResponse().equals("FAIL")){
                        mLoginView.showLoginState("Login Fail :"+response.body().getResponseMsgCd());
//                        SycretWare.getProvider().closeSession();
                    }
                    Log.d("TEST", response.body().getResponse());
                    Log.d("TEST", response.body().getResponseMsgCd());

                }
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    mLoginView.progressOnOff(false);
                    Log.d("TEST","fail :"+ t.toString());
                }
            });
        } catch (JsonProcessingException e) {
            mLoginView.progressOnOff(false);
            Log.d("TEST",e.toString());
        }
    }


}
