package com.muhanbit.sycrethealth;

/**
 * Created by hwjoo on 2017-01-18.
 */

public class Record {
    String step;
    String startTime;
    String endTime;
    String date;

    public Record(String step, String startTime, String endTime, String date) {
        this.step = step;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
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
