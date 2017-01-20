package com.muhanbit.sycrethealth.model;

import android.content.Context;

import com.muhanbit.sycrethealth.DBHandler;
import com.muhanbit.sycrethealth.Record;

import java.util.ArrayList;

import static android.R.attr.id;

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
    public long insertData(Record record) {
        DBHandler dbHandler = DBHandler.getInstance(mContext);
        long id = dbHandler.addRecord(record);
        return id;
    }

    @Override
    public Record selectLastInserted() {
        DBHandler dbHandler = DBHandler.getInstance(mContext);
        return dbHandler.selectLastRecord();
    }
}
