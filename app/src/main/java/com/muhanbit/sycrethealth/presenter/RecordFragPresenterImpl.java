package com.muhanbit.sycrethealth.presenter;

import android.view.View;

import com.muhanbit.sycrethealth.OnDeleteClickListener;
import com.muhanbit.sycrethealth.Record;
import com.muhanbit.sycrethealth.contract.AdapterContract;
import com.muhanbit.sycrethealth.model.RecordFragModel;
import com.muhanbit.sycrethealth.view.RecordFragView;

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
        this.mRecordAdapterView = mRecordAdapterView;
        this.mRecordAdapterView.setDeleteClickListener(this);
    }
    @Override
    public void setRecordAdapterModel(AdapterContract.RecordModel mRecordAdapterModel) {
        this.mRecordAdapterModel = mRecordAdapterModel;
    }

    /*
     * recyclrerview의 각 item별 delete img click
     * click 후 logic 추가
     */
    @Override
    public void onDeleteClick(int position) {
        Record record = mRecordAdapterModel.getItem(position);
        mRecordFragView.showToast(record.getStep());


    }
}
