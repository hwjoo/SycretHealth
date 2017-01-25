package com.muhanbit.sycrethealth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by hwjoo on 2017. 1. 11..
 */

public class SharedPreferenceBase {
    private static final String SP_NAME = "sycret_health";
    public static final String USER_ID="user_id";
    private Context mContext;


    public static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp;
    }



}
