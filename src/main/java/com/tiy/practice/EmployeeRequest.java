package com.tiy.practice;

/**
 * Created by crci1 on 2/9/2017.
 */
public class EmployeeRequest {
    private String name;
    private String password;
    private String firstName;
    private String lastName;
    private String position;

    public EmployeeRequest(String name, String password, String firstName) {
        this.name = name;
        this.password = password;
        this.firstName = firstName;
    }

    public EmployeeRequest() {
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
