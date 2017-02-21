package com.tiy.practice;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

/**
 * Created by crci1 on 2/4/2017.
 */

@Entity
public class Guest implements Comparable<Guest> {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private int partyof;
    private int waiting;
    private Time start_time;
    private Time time_now;
    private java.util.Date reservationDate;
    private Time reservationTime;
    private ReservationList reservationList;

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

    public Guest(String firstName, String lastName, String password, String email, int partyof, int waiting, WaitingList waitlist) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.partyof = partyof;
        this.waiting = waiting;
        this.waitlist = waitlist;
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

    public Guest(Long id, String firstName, String lastName, String password, String email, int partyof, int waiting, Time start_time, Time time_now, ReservationList reservationList, WaitingList waitlist) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.partyof = partyof;
        this.waiting = waiting;
        this.start_time = start_time;
        this.time_now = time_now;
        this.reservationList = reservationList;
        this.waitlist = waitlist;
    }

    //Many to One relationship with WaitingList class
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    public WaitingList getWaitlist() {

        return waitlist;
    }

    public void setWaitlist(WaitingList waitlist) {
        this.waitlist = waitlist;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    public ReservationList getReservationList() {
        return reservationList;
    }

    public void setReservationList(ReservationList reservationList) {
        this.reservationList = reservationList;
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

    @Column(name = "waiting")
    public int getWaiting() {

        return waiting;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    @Column(name = "start_time")
    public Time getStart_time() {
        return start_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    @Column(name = "time_now")
    public Time getTime_now() {
        return time_now;
    }

    public void setTime_now(Time time_now) {
        this.time_now = time_now;
    }

    @Override
    public int compareTo(Guest guest) {

        return (int) (this.id - guest.id);
    }

    public java.util.Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(java.util.Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Time getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Time reservationTime) {
        this.reservationTime = reservationTime;
    }
}
