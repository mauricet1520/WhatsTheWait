package com.tiy.practice;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by crci1 on 2/5/2017.
 */

@Entity
public class Restaurant {
    private Long id;
    private String name;
    private String type;
    private String address;
    private String password;
    private String email;
    private WaitingList waitingList;

    private Set<Employee> employees;

    public Restaurant() {
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
    public Set<Employee> getEmployees() {
        if (employees == null) {
            employees = new HashSet<>();
        }
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
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

