package com.muhanbit.sycrethealth.presenter;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhanbit.sycrethealth.DBHandler;
import com.muhanbit.sycrethealth.Encrypt;
import com.muhanbit.sycrethealth.OnDeleteClickListener;
import com.muhanbit.sycrethealth.Record;
import com.muhanbit.sycrethealth.SycretWare;
import com.muhanbit.sycrethealth.contract.AdapterContract;
import com.muhanbit.sycrethealth.json.EncRequest;
import com.muhanbit.sycrethealth.json.JsonResponse;
import com.muhanbit.sycrethealth.json.RecordInsertRequest;
import com.muhanbit.sycrethealth.model.RecordFragModel;
import com.muhanbit.sycrethealth.restful.ApiClient;
import com.muhanbit.sycrethealth.restful.ApiInterface;
import com.muhanbit.sycrethealth.view.RecordFragView;
import com.sycretware.crypto.Hash;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhanbit.sycrethealth.presenter.MainFragPresenterImpl.mMainFragView;


/**
 * Created by hwjoo on 2017-01-18.
 */

public class RecordFragPresenterImpl implements RecordFragPresenter,OnDeleteClickListener {
    private AdapterContract.RecordView mRecordAdapterView;
    private AdapterContract.RecordModel mRecordAdapterModel;
    private RecordFragView mRecordFragView;
    private RecordFragModel mRecordFragModel;



    public RecordFragPresenterImpl(RecordFragView mRecordFragView, RecordFragModel mRecordFragModel) {
        this.mRecordFragView = mRecordFragView;
        this.mRecordFragModel = mRecordFragModel;
    }
    @Override
    public void setRecordAdapterView(AdapterContract.RecordView mRecordAdapterView) {
        /*
         *RecordFragPresenterImpl에서 OnDeleteClickLister를 implement하였기 때문에 다형성에 의해
         * setDeleteClickListener(this)의 this는 RecordFragPresenterImpl이되고, Adapter instance
         * 변수로 OnDeleteClickListenr mOnDeleteClickListener에 reference를 전달한다.
         * 따라서, Adapter의 mOnDeleteClickListener.onDeleteClick( )을 수행하면
         * presenter내부의  onDeleteClick()이 수행된다.
         */
        this.mRecordAdapterView = mRecordAdapterView;
        this.mRecordAdapterView.setDeleteClickListener(this);
    }
    @Override
    public void setRecordAdapterModel(AdapterContract.RecordModel mRecordAdapterModel) {
        this.mRecordAdapterModel = mRecordAdapterModel;
    }
    @Override
    public void initializeRecord() {
        Handler initHandler = new Handler();
        mRecordFragView.progressOnOff(true);
        initHandler.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<Record> initRecords = mRecordFragModel.selectRecords(DBHandler.ORDER_BY_DESC);
                mRecordAdapterModel.setRecords(initRecords);
                mRecordAdapterView.notifyAdapter();
                mRecordFragView.progressOnOff(false);
            }
        });
    }


    /*
     * MainFragPresenter에서 data insert하면
     * MainFragPresenter에서 getContainer로 MainFragment의 mContainer(ContainerActivity)를
     * 전달받아 ContainerActivity의 insertedReocrd()를 수행한다.
     * insertedRecord()에서 RecordFragment로 callback을 날리고,
     * RecordFragment에서 callback method에서 이 insertedRecordAtMain()으로
     * data가 insert되었음을 알린다. 따라서 이 method에서 AdapterContract를 이용해
     * Recyclerview를 refresh한다.
     */
    @Override
    public void insertedRecordAtMain() {
        Handler refreshHandler = new Handler();
        mRecordFragView.progressOnOff(true);
        refreshHandler.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<Record> initRecords = mRecordFragModel.selectRecords(DBHandler.ORDER_BY_DESC);
                mRecordAdapterModel.setRecords(initRecords);
                mRecordAdapterView.notifyAdapter();
                mRecordFragView.progressOnOff(false);
            }
        });
    }

    @Override
    public void deleteRecord(final int position) {
        final Record recordItem = mRecordAdapterModel.getItem(position);

        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mRecordFragView.progressOnOff(true);
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                if(mRecordFragModel.deleteSelectedRecord(recordItem.getId())){//main model에서 db접근하여 data삭제

                    return true;
                }else{
                    mRecordFragView.showSnackBar("삭제 실패.");
                    return  false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                /*
                 * Sqlite insert 실패 -> tre 반환, 성공 -> false
                 * sqlite에 저장완료되면 server로 date 전송
                 */
                if(result){
                    mRecordAdapterModel.removeRecordItem(position); //adapter model의 list중 선택 postion 삭제
                    mRecordAdapterView.notifyAdapter();
                    mRecordFragView.showSnackBar("삭제되었습니다.");
                    sendDeleteRequest(recordItem);

                }else{
                    mRecordFragView.showSnackBar("삭제실패");
                    mRecordFragView.progressOnOff(false);
                }

            }
        }.execute();
    }
    @Override
    public boolean sendDeleteRequest(Record record) {
        /*
         * 추가적인 web server insert MySQL
         */
        final boolean[] requestFlag = {false};
        try {
            ObjectMapper mapper = new ObjectMapper();
            /*
             * new RecordInsertRequest("user" 는 sharedprefrence의 user id를 꺼내서 사용할예정.
             */
            RecordInsertRequest recordInsertRequest = new RecordInsertRequest(
                    "user",record.getId(),record.getStep(),record.getStartTime(),
                    record.getEndTime(),record.getDate()
            );
            String jsonString = mapper.writeValueAsString(recordInsertRequest);
            String tempTrKey = Hash.HashString((String)null, SycretWare.getDeviceIdBas64Encoded());
            String encJsonString = Encrypt.encrypt(true, jsonString,tempTrKey);

            EncRequest encRequest = new EncRequest(encJsonString);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonResponse> call = apiService.requestDeleteRecord(SycretWare.getDeviceIdBas64Encoded(), encRequest);
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
                    mRecordFragView.progressOnOff(false);
                    if(response.body().getResponse().equals("SUCCESS")){
                        mRecordFragView.showSnackBar("서버 삭제 SUCCESS");
                        requestFlag[0] =true;
                    }else if(response.body().getResponse().equals("FAIL")){
                        mRecordFragView.showSnackBar("서버 저장 FAIL");
                    }
                    Log.d("TEST", response.body().getResponse());
                    Log.d("TEST", response.body().getResponseMsgCd());

                }
                @Override
                public void onFailure(Call<JsonResponse> call, Throwable t) {
                    mRecordFragView.progressOnOff(false);
                    Log.d("TEST","fail :"+ t.toString());
                }
            });
        } catch (JsonProcessingException e) {
            mRecordFragView.progressOnOff(false);
            Log.d("TEST",e.toString());
        }

        return requestFlag[0];
    }

    /*
     * OnDeleteClickListener Override method
     * recyclrerview의 각 item별 delete img click
     * click 후 logic 추가
     */
    @Override
    public void onDeleteClick(int position) {
        mRecordFragView.showDeleteDialog(position);
       /*
        * DeleteDialog에서 삭제 버튼 클릭시, 이 presenter의
        * deleteRecord로 다시 return되어 돌아온다.
        */
    }


}
