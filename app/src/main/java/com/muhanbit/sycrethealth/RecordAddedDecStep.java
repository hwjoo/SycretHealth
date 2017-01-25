package com.muhanbit.sycrethealth;

/**
 * Created by hwjoo on 2017-01-23.
 */

public class RecordAddedDecStep extends Record {
    int id;
    String step;
    String decStep;
    String startTime;
    String endTime;
    String date;

    public RecordAddedDecStep(String step, String decStep,String startTime, String endTime, String date, int id) {
        this.step = step;
        this.decStep = decStep;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.id = id;
    }

    public String getDecStep() {
        return decStep;
    }

    public void setDecStep(String decStep) {
        this.decStep = decStep;
    }

    @Override
    public String getStep() {
        return this.step;
    }

    @Override
    public void setStep(String step) {
        this.step = step;
    }

    @Override
    public String getStartTime() {
        return this.startTime;
    }

    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String getEndTime() {
        return this.endTime;
    }

    @Override
    public void setEndTime(String endTime) {
       this.endTime = endTime;
    }

    @Override
    public String getDate() {
        return this.date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
