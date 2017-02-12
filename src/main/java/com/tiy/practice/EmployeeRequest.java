package com.tiy.practice;

/**
 * Created by crci1 on 2/9/2017.
 */
public class EmployeeRequest {
    private String name;
    private String password;
    private String employeeName;

    public EmployeeRequest(String name, String password, String employeeName) {
        this.name = name;
        this.password = password;
        this.employeeName = employeeName;
    }

    public EmployeeRequest() {
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
