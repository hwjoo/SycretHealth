package com.muhanbit.sycrethealth.model;

import com.muhanbit.sycrethealth.Record;

import java.util.ArrayList;

/**
 * Created by hwjoo on 2017. 1. 18..
 */

public interface MainFragModel {
    long insertData(Record record);
    Record selectLastInserted();
}
