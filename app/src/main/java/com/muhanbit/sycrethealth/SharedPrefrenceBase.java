package com.muhanbit.sycrethealth;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hwjoo on 2017. 1. 11..
 */

public class SharedPrefrenceBase {
    private String SP_NAME = "sycret_health";
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    public SharedPrefrenceBase(Context context) {
        mSharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }
    public SharedPreferences.Editor getEditor(){

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        return editor;
    }
}
