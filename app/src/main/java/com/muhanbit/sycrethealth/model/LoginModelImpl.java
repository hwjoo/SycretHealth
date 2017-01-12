package com.muhanbit.sycrethealth.model;

import android.content.Context;

import com.muhanbit.sycrethealth.json.LoginRequest;

/**
 * Created by hwjoo on 2017. 1. 11..
 */

public class LoginModelImpl implements LoginModel {
    public Context mContext;
    public LoginModelImpl(Context context){
        this.mContext = context;
    }
}
