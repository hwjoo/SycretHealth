package com.muhanbit.sycrethealth.json;

/**
 * Created by hwjoo on 2017-01-12.
 */

public class LoginRequest {
    String userid;
    String password;

    public LoginRequest(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
