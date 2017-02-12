package com.tiy.practice;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

/**
 * Created by crci1 on 2/4/2017.
 */

@Entity
public class Guest {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private int partyof;
    private WaitingList waitlist;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Guest() {
    }

    public Guest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Guest(String firstName, String lastName, String email, String password, int partyof) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.partyof = partyof;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    public WaitingList getWaitlist() {
        return waitlist;
    }

    public void setWaitlist(WaitingList waitlist) {
        this.waitlist = waitlist;
    }

    @Column(name = "party_size")
    public int getPartyof() {
        return partyof;
    }

    public void setPartyof(int partyof) {
        this.partyof = partyof;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
