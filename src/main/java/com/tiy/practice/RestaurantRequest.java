package com.tiy.practice;

/**
 * Created by crci1 on 2/21/2017.
 */
public class RestaurantRequest {

    private String name;
    private String type;
    private String address;
    private String password;
    private String email;

    public RestaurantRequest(String name, String type, String address, String password, String email) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.password = password;
        this.email = email;
    }

    public RestaurantRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
