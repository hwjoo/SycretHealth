package com.muhanbit.sycrethealth;

/**
 * Created by hwjoo on 2017-01-18.
 */

public class Record {
    /*
     * recyclreview item & Database 전달 객체로 사용
     *  id는 sqlite primary key값 저장, sqlite 내부 data 삭제시 id값 이용
     */
    int id;
    String step;
    String startTime;
    String endTime;
    String date;

    public Record(){

    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
