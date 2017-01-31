package com.muhanbit.sycrethealth.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.muhanbit.sycrethealth.SharedPreferenceBase;
import com.muhanbit.sycrethealth.json.LoginRequest;

/**
 * Created by hwjoo on 2017. 1. 11..
 */

public class LoginModelImpl implements LoginModel {
    public Context mContext;
    public LoginModelImpl(Context context){
        this.mContext = context;
    }

    @Override
    public void putUserIdAtSp(String userId) {
        SharedPreferences sp = SharedPreferenceBase.getSharedPreferences(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SharedPreferenceBase.USER_ID, userId);
        editor.apply();
    }
}
