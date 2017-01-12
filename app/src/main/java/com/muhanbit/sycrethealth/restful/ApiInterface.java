package com.muhanbit.sycrethealth.restful;

import com.muhanbit.sycrethealth.json.EncRequest;
import com.muhanbit.sycrethealth.json.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hwjoo on 2017. 1. 11..
 */

public interface ApiInterface {
    @POST("/")
    Call<LoginResponse> requestLogin(@Body EncRequest loginEncRequest);


}
