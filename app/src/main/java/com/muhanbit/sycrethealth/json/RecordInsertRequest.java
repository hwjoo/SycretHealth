package com.muhanbit.sycrethealth.json;

/**
 * Created by hwjoo on 2017-01-20.
 */

public class RecordInsertRequest {
    String userId;
    int id;
    String step;
    String startTime;
    String endTime;
    String date;

    public RecordInsertRequest(String userId, int id, String step, String startTime, String endTime, String date) {
        this.userId = userId;
        this.id = id;
        this.step = step;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
