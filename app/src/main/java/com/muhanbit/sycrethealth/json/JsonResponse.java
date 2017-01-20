package com.muhanbit.sycrethealth.json;

/**
 * Created by hwjoo on 2017-01-12.
 */

public class JsonResponse {
    String  response;
    String responseMsgCd;

    public JsonResponse(){

    }

    public JsonResponse(String response, String responseMsgCd) {
        this.response = response;
        this.responseMsgCd = responseMsgCd;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponseMsgCd() {
        return responseMsgCd;
    }

    public void setResponseMsgCd(String responseMsgCd) {
        this.responseMsgCd = responseMsgCd;
    }
}
