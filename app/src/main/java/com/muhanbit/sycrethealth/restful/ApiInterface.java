package com.muhanbit.sycrethealth.restful;

import com.muhanbit.sycrethealth.json.EncRequest;
import com.muhanbit.sycrethealth.json.JsonResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hwjoo on 2017. 1. 11..
 */

public interface ApiInterface {
    @POST("/healthcare/Login/{deviceId}")
    Call<JsonResponse> requestLogin(
            @Path("deviceId") String deviceId,
            @Body EncRequest loginEncRequest);

    @POST("/healthcare/Record/Register/{deviceId}")
    Call<JsonResponse> requestInsertRecord(
            @Path("deviceId") String deviceId,
            @Body EncRequest insertEncRequest);

    @POST("/healthcare/Record/Delete/{deviceId}")
    Call<JsonResponse> requestDeleteRecord(
            @Path("deviceId") String deviceId,
            @Body EncRequest delteEncRequest);

}
