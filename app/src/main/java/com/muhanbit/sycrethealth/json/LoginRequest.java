package com.muhanbit.sycrethealth.json;

/**
 * Created by hwjoo on 2017-01-12.
 */

public class LoginRequest {
    String userId;
    String password;

    public LoginRequest(String userid, String password) {
        this.userId = userid;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
