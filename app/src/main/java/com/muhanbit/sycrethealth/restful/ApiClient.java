package com.muhanbit.sycrethealth.restful;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by hwjoo on 2017. 1. 11..
 */

public class ApiClient {
//    private static final String BASE_URL = "http://192.168.1.108:8080";
    private static final String BASE_URL = "http://192.168.100.168:8080";
    private static Retrofit mRetrofit;
    private ApiClient(){

    }

    public static Retrofit getClient(){

        if(mRetrofit == null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS);
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(builder.build())
                    .build();
        }
        return mRetrofit;
    }
}
