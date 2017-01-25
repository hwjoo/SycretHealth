package com.muhanbit.sycrethealth;

import java.util.ArrayList;

/**
 * Created by hwjoo on 2017-01-24.
 */

public class Log {
    private static ArrayList<String> requestLogs;
    private static ArrayList<String> responseLogs;
    private static Log mLog;

    public static Log getInstance(){
        if(mLog == null){
            mLog = new Log();
            requestLogs = new ArrayList<>();
            responseLogs = new ArrayList<>();
        }
        return mLog;
    }
    public void addRequestLog(String log){
        requestLogs.add(log);
    }
    public void addResponseLog(String log){
        responseLogs.add(log);
    }
    public ArrayList<String> getRequestLogs(){
        return requestLogs;
    }
    public ArrayList<String> getResponseLogs(){
        return responseLogs;
    }
}
