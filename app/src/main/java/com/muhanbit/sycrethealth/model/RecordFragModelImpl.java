package com.muhanbit.sycrethealth.model;

import android.content.Context;

import com.muhanbit.sycrethealth.DBHandler;
import com.muhanbit.sycrethealth.Record;

import java.util.ArrayList;

/**
 * Created by hwjoo on 2017-01-18.
 */

public class RecordFragModelImpl implements RecordFragModel {
    Context mContext;
    public RecordFragModelImpl(Context context){
        this.mContext = context;
    }
    @Override
    public ArrayList<Record> selectRecords(int selectOrder) {
        DBHandler dbHandler = DBHandler.getInstance(mContext);
        /*
         * selectAllRecord size 0이면 data 없음
         */
        return dbHandler.selectAllRecord(selectOrder);
    }

    @Override
    public boolean deleteSelectedRecord(int primaryKey) {
        DBHandler dbHandler = DBHandler.getInstance(mContext);
        /*
         * 삭제성공 -> true / 실패 -> false 반환
         */
        return dbHandler.deleteRecord(primaryKey);
    }

    @Override
    public Record selectLastInserted() {
        DBHandler dbHandler = DBHandler.getInstance(mContext);
        return dbHandler.selectLastRecord();
    }
}
