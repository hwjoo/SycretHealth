package com.muhanbit.sycrethealth.presenter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhanbit.sycrethealth.Encrypt;
import com.muhanbit.sycrethealth.PedometerService;
import com.muhanbit.sycrethealth.Record;
import com.muhanbit.sycrethealth.SharedPreferenceBase;
import com.muhanbit.sycrethealth.SycretWare;
import com.muhanbit.sycrethealth.json.EncRequest;
import com.muhanbit.sycrethealth.json.JsonResponse;
import com.muhanbit.sycrethealth.json.RecordInsertRequest;
import com.muhanbit.sycrethealth.model.MainFragModel;
import com.muhanbit.sycrethealth.restful.ApiClient;
import com.muhanbit.sycrethealth.restful.ApiInterface;
import com.muhanbit.sycrethealth.view.MainFragView;
import com.sycretware.crypto.Hash;
import com.sycretware.obj.ExportKey;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hwjoo on 2017-01-16.
 */

public class MainFragPresenterImpl implements MainFragPresenter {
    public static MainFragView mMainFragView;
    public MainFragModel mMainFragModel;
    private boolean startFlag;


    public MainFragPresenterImpl(MainFragView fragView, MainFragModel mainFragModel) {
        this.mMainFragView = fragView;
        this.mMainFragModel = mainFragModel;
        startFlag = true;
    }
    public static MainFragView getmMainFragView(){
        return mMainFragView;
    }

    @Override
    public void clickProgress() {
        mMainFragView.showTimePicker();
    }

    @Override
    public void clickOverflow() {
        mMainFragView.showPopupMenu();
    }

    @Override
    public void initCircleProgress(CircleProgressView circleProgressView) {
        float remainTime = circleProgressView.getCurrentValue();
        circleProgressView.setTextMode(TextMode.TEXT);
        circleProgressView.setUnitVisible(false);
        if(remainTime == 0) {
            circleProgressView.setValue(0);
        }else{
            circleProgressView.setValueAnimated(remainTime, 0, 1000);
        }
        circleProgressView.setText("0시간 0분");
    }
    @Override
    public void initStep(TextView stepText) {
        stepText.setText("0 Step");
    }
    @Override
    public void setTimeForProgress(CircleProgressView circleProgressView ,int hour, int minute) {
        int totalMinute = hour*60 + minute;
        circleProgressView.setMaxValue(totalMinute);
        circleProgressView.setUnitVisible(false);
        circleProgressView.setValueAnimated(totalMinute);
        circleProgressView.setText(hour+"시간 "+minute+"분");
    }

    @Override
    public void clickResetMenu(CircleProgressView circleProgressView) {
        this.initCircleProgress(circleProgressView);
    }

    @Override
    public void clickSetMenu() {
        mMainFragView.showTimePicker();
    }

    @Override
    public boolean clickActionBtn() {

        if(mMainFragView.getCurrentMinute()==0){
            mMainFragView.showSnackBar("시간설정 후 시작하세요.");
            return false;
        }
        if(!this.isServiceRunningCheck("com.muhanbit.sycrethealth.PedometerService")){
            startFlag = true;
        }

        Intent intent = new Intent(mMainFragView.getViewContext(), PedometerService.class);
        if(startFlag && !this.isServiceRunningCheck("com.muhanbit.sycrethealth.PedometerService")){
            intent.putExtra("total_minute",mMainFragView.getCurrentMinute());
            mMainFragView.getViewContext().startService(intent);
            mMainFragView.changeBtnText("STOP");
            startFlag=false;
        }else{
            mMainFragView.getViewContext().stopService(intent);
//            mMainFragView.changeBtnText("START");
//            mMainFragView.showSaveDialog();
            startFlag = true;
        }
        return true;
    }

    /*
     * service 실행여부 판단.
     */
    @Override
    public boolean isServiceRunningCheck(String servicename) {
        ActivityManager manager = (ActivityManager) mMainFragView.getViewContext().getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (servicename.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    /*
     * step을 local key를 이용하여 encryption 후 data 저장.
     */
    @Override
    public boolean insertStepInfo(final String step, final String startTime, final String endTime, final String date) {
        final boolean[] insertFlag = {false};

        new AsyncTask<Void,Void,Record>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mMainFragView.progressOnOff(true);
            }

            @Override
            protected Record doInBackground(Void... voids) {
                ExportKey localKey = SycretWare.getEncryptionKey(SycretWare.LOCAL_KEY);
                String encStep =SycretWare.getProvider().encrypt.encryptBase64(
                        com.sycretware.security.Encrypt.ENCRYPT, step,"UTF-8", localKey);
                Record saveRecord = new Record(encStep,startTime,endTime,date);
                Record savedRecord = null;
                if(mMainFragModel.insertData(saveRecord)> -1){
                    savedRecord = mMainFragModel.selectLastInserted();
                    /*
                     * server 측에 step을 plain text로 보내기 위해 저장된 값을 복호화하여 보낸다.
                     * 이 부분은 추후 수정해야한다. 암호화 과정이 오래걸리기때문에 수정해야한다.
                     */
                    String decStep =SycretWare.getProvider().encrypt.encryptBase64(
                            com.sycretware.security.Encrypt.DECRYPT, savedRecord.getStep(),"UTF-8", localKey);
                    savedRecord.setStep(decStep);
                }

                return savedRecord;
            }

            @Override
            protected void onPostExecute(Record result) {
                super.onPostExecute(result);
                /*
                 * Sqlite insert 실패 -> -1 반환, 성공 -> 1 이상 반환(0 미포함)
                 * 따라서 반환 id가 -1보다 크면 성공
                 * sqlite에 저장완료되면 server로 date 전송
                 */
                if(result !=null){
                    mMainFragView.showToast("내부 SQLite 저장 완료");
                    mMainFragView.getContainer().insertedRecord(); //insert되었다는것은 RecordFragment로 전달
                    insertFlag[0] = true;
                    sendInsertRequest(result);

                }else{
                    mMainFragView.showSnackBar("DATA 저장 error");
                    mMainFragView.progressOnOff(false);
                }

            }
        }.execute();


        return insertFlag[0];
    }
    @Override
    public boolean sendInsertRequest(Record record) {
        /*
         * 추가적인 web server insert MySQL
         */
        final boolean[] requestFlag = {false};
        try {
            final ObjectMapper mapper = new ObjectMapper();
            /*
             * new RecordInsertRequest("user" 는 sharedprefrence의 user id를 꺼내서 사용할예정.
             */
            String userId = SharedPreferenceBase.getSharedPreferences(mMainFragView.getViewContext())
                    .getString(SharedPreferenceBase.USER_ID,"");
            RecordInsertRequest recordInsertRequest = new RecordInsertRequest(
                    userId, record.getId(), record.getStep(), record.getStartTime(),
                    record.getEndTime(),record.getDate()
            );
            ExportKey trKey = SycretWare.getEncryptionKey(SycretWare.TRAFFIC_KEY);
            final String jsonString = mapper.writeValueAsString(recordInsertRequest);
            String encJsonString =SycretWare.getProvider().encrypt.encryptBase64(
                    com.sycretware.security.Encrypt.ENCRYPT, jsonString, "UTF-8", trKey);

            EncRequest encRequest = new EncRequest(encJsonString);
            final String encJsonForm = mapper.writeValueAsString(encRequest);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<JsonResponse> call = apiService.requestInsertRecord(SycretWare.getUrlEncodedDeviceId(), encRequest);
            Log.d("TEST", String.valueOf(call.request().url()));
            call.enqueue(new Callback<JsonResponse>() {
                @Override
                public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                    /*
                     * responseMsgCd :
                     *
                     */
                    JsonResponse insertResponse = response.body();
                    String responseState= insertResponse.getResponse();
                    String responseMsgCd= insertResponse.getResponseMsgCd();
                    mMainFragView.progressOnOff(false);
                    if(response.body().getResponse().equals("SUCCESS")){
                        mMainFragView.showSnackBar("서버 저장 SUCCESS");
                        requestFlag[0] =true;

                    }else if(response.body().getResponse().equals("FAIL")){
                        mMainFragView.showSnackBar("서버 저장 FAIL");
                    }
                    Log.d("TEST", response.body().getResponse());
                    Log.d("TEST", response.body().getResponseMsgCd());
                    /*
                     * Log 남기기
                     */
                    try {
                        String responseString = mapper.writeValueAsString(insertResponse);
                        com.muhanbit.sycrethealth.Log log = com.muhanbit.sycrethealth.Log.getInstance();
                        log.addRequestLog("Insert Json Request :"+jsonString);
                        log.addRequestLog("Insert EncJson Request :"+encJsonForm);
                        log.addResponseLog("Insert Json Response :"+responseString);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(Call<JsonResponse> call, Throwable t) {
                    mMainFragView.progressOnOff(false);
                    Log.d("TEST","fail :"+ t.toString());
                }
            });
        } catch (JsonProcessingException e) {
            mMainFragView.progressOnOff(false);
            Log.d("TEST",e.toString());
        }

        return requestFlag[0];
    }



}
