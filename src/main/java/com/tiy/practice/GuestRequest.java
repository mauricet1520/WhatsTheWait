package com.tiy.practice;

/**
 * Created by crci1 on 2/9/2017.
 */
public class GuestRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private long location;
    private int partyof;


    public GuestRequest() {
    }

    public GuestRequest(String firstName, String lastName, String password, String email, long location, int partyof) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.location = location;
        this.partyof = partyof;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public int getPartyof() {
        return partyof;
    }

    public void setPartyof(int partyof) {
        this.partyof = partyof;
    }
}
