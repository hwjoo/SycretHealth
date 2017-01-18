package com.muhanbit.sycrethealth.contract;

import com.muhanbit.sycrethealth.OnDeleteClickListener;
import com.muhanbit.sycrethealth.Record;

import java.util.ArrayList;

/**
 * Created by hwjoo on 2017-01-18.
 */

public interface AdapterContract {
    interface RecordView{
        void notifyAdapter();
        void setDeleteClickListener(OnDeleteClickListener listener);
    }
    interface RecordModel{
        void addRecordItem(ArrayList<Record> records);
        void clearItem();
        Record getItem(int position);
    }
}
