package com.muhanbit.sycrethealth.presenter;

import com.muhanbit.sycrethealth.contract.AdapterContract;

/**
 * Created by hwjoo on 2017-01-18.
 */

public interface RecordFragPresenter {
    void setRecordAdapterView(AdapterContract.RecordView mRecordAdapterView);
    void setRecordAdapterModel(AdapterContract.RecordModel mRecordAdapterModel);
}
