package com.muhanbit.sycrethealth.restful;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by hwjoo on 2017. 1. 11..
 */

public class ApiClient {
    private static final String BASE_URL = "";
    private static Retrofit mRetrofit;
    private ApiClient(){

    }

    public static Retrofit getClient(){
        if(mRetrofit == null){
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}
