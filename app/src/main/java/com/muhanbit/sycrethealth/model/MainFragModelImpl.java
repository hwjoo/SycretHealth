package com.muhanbit.sycrethealth.model;

import android.content.Context;

import com.muhanbit.sycrethealth.DBHandler;
import com.muhanbit.sycrethealth.Record;

/**
 * Created by hwjoo on 2017. 1. 18..
 */

public class MainFragModelImpl implements MainFragModel {
    private Context mContext;
    /*
     * database insert
     */

    public MainFragModelImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void insertData(Record record) {
        DBHandler dbHandler = DBHandler.getInstance(mContext,1);
        dbHandler.addRecord(record);

    }

    @Override
    public boolean deleteData(Record record) {
        DBHandler dbHandler = DBHandler.getInstance(mContext,1);
        if(dbHandler.deleteRecord(record)){
            return true;
        }
        return false;
    }

}
