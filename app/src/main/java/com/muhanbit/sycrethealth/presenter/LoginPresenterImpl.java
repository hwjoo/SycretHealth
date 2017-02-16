package com.muhanbit.sycrethealth.presenter;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhanbit.sycrethealth.Encrypt;
import com.muhanbit.sycrethealth.SharedPreferenceBase;
import com.muhanbit.sycrethealth.SycretWare;
import com.muhanbit.sycrethealth.json.EncRequest;
import com.muhanbit.sycrethealth.json.LoginRequest;
import com.muhanbit.sycrethealth.json.JsonResponse;
import com.muhanbit.sycrethealth.model.LoginModel;
import com.muhanbit.sycrethealth.restful.ApiClient;
import com.muhanbit.sycrethealth.restful.ApiInterface;
import com.muhanbit.sycrethealth.view.LoginView;
import com.sycretware.auth.KeyStore;
import com.sycretware.auth.Provider;
import com.sycretware.crypto.Hash;
import com.sycretware.exception.PersonalizationDeviceUnknownException;
import com.sycretware.obj.ExportKey;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by hwjoo on 2017-01-11.
 */

/*
 * Login process : 먼저 pin 인증 후 상태체크 완료 후 유저아이디, 패스워드를 통해 로그인 시도.
 *                 실제로 로그인 할 경우도 traffic key를 이용하여 encryption 후 data를 주고
 *                 받기 위한 것.
 * -login form : userId, password, pin number(MicroSD card)
 *   -> 로그인 할 경우 sd card의 pin인증 후 sd card 상태 체크
 *   -> 상태체크란 sd card 내부에 local key , traffic key 존재 여부 확인
 *   -> key 존재 여부에 대한 예외처리 필수.
 *   -> traffic key같은 경우 존재하지 않을때, App server에 traffic key 발급 요청
 */

public class LoginPresenterImpl implements LoginPresenter {
    private static final String PERSONA_URL ="http://192.168.100.168:8080/healthcare";
    private static final String DB_KEY = "db_key";
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
                        ExportKey localKey = provider.keyStore.getSecretKey(KeyStore.KEY_LOCAL);
                        if(localKey == null){
                            errorCode = "local key not found";
                            return false;
                        }
                        ExportKey trKey = provider.keyStore.getSecretKey(KeyStore.KEY_TRAFFIC);

                        Log.d("TEST","serial num : "+SycretWare.getDeviceSerial());
                        if(provider.data.getStoreKey() == null){
                            String dbKey = UUID.randomUUID().toString();
                            Log.d("TEST", "dbkey put : "+ dbKey);
                            Log.d("TEST", "dbkey put SIZE : "+ dbKey.getBytes().length);
                            try {
                                provider.data.putStoreKey(dbKey.getBytes("UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }else{
                            byte[] dbByteKey =provider.data.getStoreKey();
                            Log.d("TEST", "dbkey get size : "+ dbByteKey.length);
                            try {
                                String dbKey =  new String(dbByteKey,0, 36,"UTF-8");
                                Log.d("TEST", "dbkey get : "+ dbKey);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        if(trKey == null){
                          /*
                         * start
                         * test 진행 후 삭제
                         */

//                        if(true){
                        /*
                         * end
                         * test 진행 후 삭제
                         */
                            if(provider.secureConnect.connectEnroll(PERSONA_URL, userId, password)){
                                if(provider.data.getStoreKey() == null){
                                    String dbKey = UUID.randomUUID().toString();
                                    String encDbKey = provider.encrypt.encryptBase64(com.sycretware.security.Encrypt.ENCRYPT, dbKey,"UTF-8", localKey);
                                    try {
                                        if(provider.data.putStoreKey(encDbKey.getBytes("UTF-8"))){
                                            Log.d("TEST"," put db key 완료, db key :" + dbKey);
                                        }
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
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
     *
     *  tempTrKey는 실제 trkey를 대체하여 사용. tempTrKey = deviceid를 base64 encoding한 값을 hash하여
     *  key로 사용.
     */
    @Override
    public void sendLoginRequest(final String userId, String password) {

        try {
            ExportKey trKey = SycretWare.getEncryptionKey(SycretWare.TRAFFIC_KEY);
            Log.d("TEST", "trkey :"+String.valueOf(trKey.getKey()));
            String pwHash = Hash.HashString((String)null,password);
            final LoginRequest loginRequest = new LoginRequest(userId,pwHash);
            final ObjectMapper mapper = new ObjectMapper();

            final String jsonString = mapper.writeValueAsString(loginRequest);
            String encJsonString =SycretWare.getProvider().encrypt.encryptBase64(
                    com.sycretware.security.Encrypt.ENCRYPT, jsonString, "UTF-8", trKey);

            final EncRequest encRequest = new EncRequest(encJsonString);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<JsonResponse> call = apiService.requestLogin(SycretWare.getUrlEncodedDeviceId(), encRequest);
            Log.d("TEST", String.valueOf(call.request().url()));
            call.enqueue(new Callback<JsonResponse>() {
                @Override
                public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                    /*
                     * responseMsgCd :
                     * USER_NOTFOUND (사용자존재 하지않을경우)
	                 * USER_AUTH_FAIL (사용자 인증 실패)
	                 * SYSTEM_ERROR (시스템 에러 Exception )
                     */
                    JsonResponse loginResponse = null;
                    String responseState;
                    String responseMsgCd;
                    try{
                        loginResponse = response.body();
                        responseState= loginResponse.getResponse();
                        responseMsgCd= loginResponse.getResponseMsgCd();
                    }catch (NullPointerException e){
                        mLoginView.showLoginState("response nullpointer excption");
                        mLoginView.progressOnOff(false);
                        return;
                    }
                    mLoginView.progressOnOff(false);
                    if(response.body().getResponse().equals("SUCCESS")){
                        mLoginView.showLoginState("Login success");
//                        Intent intent = new Intent(mLoginView.getViewContext(), ContainerActivity.class);
//                        mLoginView.startNextActivity(intent);
                        /*
                         * test용, 실제 json과 encJson을 textView에 show.
                         * 이후 확인버튼 visible -> click시 nextActivity
                         */
                        try {
                            String encJsonForm = mapper.writeValueAsString(encRequest);
                            String responseString = mapper.writeValueAsString(loginResponse);
                            mLoginView.showInvisibleWidget(jsonString, encJsonForm, responseString);
                            /*
                             * Tab 3. Log 남기기.
                             */
                            com.muhanbit.sycrethealth.Log log = com.muhanbit.sycrethealth.Log.getInstance();
                            log.addRequestLog("Login Json Request :"+jsonString);
                            log.addRequestLog("Login EncJson Request :"+encJsonForm);
                            log.addResponseLog("Login Json Response :"+responseString);

                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        /*
                         * Login 성공 후 SharedPreference에 user id 저장.
                         * 이후 server 통신시 사용
                         */
                        mLoginModel.putUserIdAtSp(userId);



                    }else if(response.body().getResponse().equals("FAIL")){
                        mLoginView.showLoginState("Login Fail :"+response.body().getResponseMsgCd());
                        try {
                            String encJsonForm = mapper.writeValueAsString(encRequest);
                            String responseString = mapper.writeValueAsString(loginResponse);
                            mLoginView.showInvisibleWidget(jsonString, encJsonForm, responseString);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("TEST", response.body().getResponse());
                    Log.d("TEST", response.body().getResponseMsgCd());

                }
                @Override
                public void onFailure(Call<JsonResponse> call, Throwable t) {
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
