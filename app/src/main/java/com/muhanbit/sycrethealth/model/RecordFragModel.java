package com.muhanbit.sycrethealth.model;

import com.muhanbit.sycrethealth.Record;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by hwjoo on 2017-01-18.
 */

public interface RecordFragModel {

    ArrayList<Record> selectRecords(int selectOrder);
    boolean deleteSelectedRecord(int primaryKey);
}
