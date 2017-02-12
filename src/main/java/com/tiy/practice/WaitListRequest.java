package com.tiy.practice;

/**
 * Created by crci1 on 2/9/2017.
 */
public class WaitListRequest {
    private String name;
    private String email;
    private String password;

    public WaitListRequest() {
    }

    public WaitListRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
