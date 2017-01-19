package com.muhanbit.sycrethealth;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.sycretware.auth.DataStore;
import com.sycretware.auth.Environment;
import com.sycretware.auth.KeyStore;
import com.sycretware.auth.Provider;
import com.sycretware.auth.ShareData;
import com.sycretware.obj.ExportKey;
import com.sycretware.security.*;

import java.io.UnsupportedEncodingException;

import static android.util.Base64.encodeToString;

/**
 * Created by hwjoo on 2017. 1. 11..
 */

public class SycretWare {
    public static final int LOCAL_KEY = 1;
    public static final int TRAFFIC_KEY = 2;
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
    public static ExportKey getEncryptionKey(int keyName){
        if(keyName == LOCAL_KEY){
            return mProvider.keyStore.getSecretKey(KeyStore.KEY_LOCAL);
        }else if(keyName == TRAFFIC_KEY){
            return  mProvider.keyStore.getSecretKey(KeyStore.KEY_TRAFFIC);
        }else{
            return null;
        }
    }
    public static String getDeviceSerial(){
        return mProvider.deviceSerial();
    }

    public static void init(Context context) {
        Environment.initialize(context);
        isInit = true;
    }
    public static String getDeviceIdBas64Encoded(){
        String encodedString = Base64.encodeToString(mProvider.data.getDeviceID(),Base64.NO_WRAP);


        return encodedString;
    }
    public static String getDBKey(){
        String decDBKey = "";
        try {
            String encDBKey = new String(mProvider.data.getStoreKey(), "UTF-8");
            decDBKey = mProvider.encrypt.encryptBase64(com.sycretware.security.Encrypt.DECRYPT,
                    encDBKey,"UTF-8", mProvider.keyStore.getSecretKey(KeyStore.KEY_LOCAL));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return decDBKey;
    }
    public static boolean isInit(){
        return isInit;
    }


}
