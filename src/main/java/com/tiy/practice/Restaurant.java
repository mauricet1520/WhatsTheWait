package com.tiy.practice;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.*;

@Entity
public class Restaurant {
    private Long id;
    private String name;
    private String type;
    private String address;
    private String password;
    private String email;
    private WaitingList waitingList;
    private ReservationList reservationList;
    private List<Employee> employees;

    public Restaurant() {

    }

    public Restaurant(Long id, String name, String type, String address, String password, String email, WaitingList waitingList, ReservationList reservationList, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.address = address;
        this.password = password;
        this.email = email;
        this.waitingList = waitingList;
        this.reservationList = reservationList;
        this.employees = employees;
    }

    public Restaurant(String name, String type, String address, String password, String email, WaitingList waitingList, ReservationList reservationList, List<Employee> employees) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.password = password;
        this.email = email;
        this.waitingList = waitingList;
        this.reservationList = reservationList;
        this.employees = employees;
    }

    public Restaurant(String name, String type, String address, String password, String email) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.password = password;
        this.email = email;
    }

    public Restaurant(int employeeId, String name, String type, String address, String password, String email) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.password = password;
        this.email = email;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "restaurant")
    public WaitingList getWaitingList() {
        if (waitingList == null){
            waitingList = new WaitingList();
        }
        return waitingList;
    }

    public void setWaitingList(WaitingList waitingList) {
        this.waitingList = waitingList;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurant")
    @JsonManagedReference
    public List<Employee> getEmployees() {
        if (employees == null) {
            employees = new ArrayList<>();
        }
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "restaurant")
    public ReservationList getReservationList() {
        if (reservationList == null)
            reservationList = new ReservationList();
        return reservationList;
    }

    public void setReservationList(ReservationList reservationList) {
        this.reservationList = reservationList;
    }

    @Column(name = "restaurant_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "food_type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "street_address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}

