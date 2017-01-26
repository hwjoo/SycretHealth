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
import java.net.URLEncoder;

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
    public static String getUrlEncodedDeviceId(){
        String urlEncodedDeviceId = "";
        try {
            urlEncodedDeviceId = URLEncoder.encode(getDeviceIdBas64Encoded(),"UTF-8");
            Log.d("TEST", "Base64 deviceId : "+SycretWare.getDeviceIdBas64Encoded());
            Log.d("TEST", "URLEncoded deviceId : "+urlEncodedDeviceId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlEncodedDeviceId;
    }
    public static String getDBKey(){
        /*
         * put dbkey byte array length 36
         * default getStoreKey()-> byte array length 48
         * 따라서 아래와 같이 new String으로 36size로 잘라서 사용.
         */
        String dbKey = "";
        byte[] dbByteKey =mProvider.data.getStoreKey();
        Log.d("TEST", "dbkey get size : "+ dbByteKey.length);
        try {
            dbKey =  new String(dbByteKey,0, 36,"UTF-8");
            Log.d("TEST", "dbkey get : "+ dbKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return dbKey;
    }
    public static boolean isInit(){
        return isInit;
    }


}
