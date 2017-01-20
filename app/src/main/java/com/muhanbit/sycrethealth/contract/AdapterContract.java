package com.muhanbit.sycrethealth.contract;

import com.muhanbit.sycrethealth.OnDeleteClickListener;
import com.muhanbit.sycrethealth.Record;

import java.util.ArrayList;

/**
 * Created by hwjoo on 2017-01-18.
 */

public interface AdapterContract {
    /*
     * adapter
     */
    interface RecordView{
        void notifyAdapter();
        void setDeleteClickListener(OnDeleteClickListener listener);
    }
    interface RecordModel{
        void setRecords(ArrayList<Record> records);
        void addRecordItem(Record record);
        void removeRecordItem(int position);
        void clearItem();
        Record getItem(int position);
    }
}
