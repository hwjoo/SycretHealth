package com.muhanbit.sycrethealth;

import android.content.Context;

import com.sycretware.auth.Environment;
import com.sycretware.auth.Provider;

/**
 * Created by hwjoo on 2017. 1. 11..
 */

public class SycretWare {
    private static Provider mProvider;
    private static boolean isInit = false;
    private SycretWare(){

    }
    public static Provider getProvider(){
        if(mProvider == null){
            mProvider = new Provider();
        }
        return mProvider;

    }

    public static void init(Context context) {
        Environment.initialize(context);
        isInit = true;
    }
    public static boolean isInit(){
        return isInit;
    }


}
