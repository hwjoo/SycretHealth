package com.muhanbit.sycrethealth.restful;

import android.util.Base64;

import com.muhanbit.sycrethealth.SycretWare;
import com.muhanbit.sycrethealth.json.EncRequest;
import com.muhanbit.sycrethealth.json.LoginResponse;
import com.sycretware.auth.DataStore;
import com.sycretware.lib.Session;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hwjoo on 2017. 1. 11..
 */

public interface ApiInterface {

    @POST("/healthcare/Login/{deviceId}")
    Call<LoginResponse> requestLogin(
            @Path("deviceId") String deviceId,
            @Body EncRequest loginEncRequest);

}
