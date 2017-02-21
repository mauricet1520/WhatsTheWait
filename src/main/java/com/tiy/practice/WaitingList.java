package com.tiy.practice;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


import javax.persistence.*;
import java.util.*;

/**
 * Created by crci1 on 2/7/2017.
 */

@Entity
public class WaitingList {
    private Long restaurantId;
    private Restaurant restaurant;
    private List<Guest> listOfUsers = new ArrayList<>();
    private int waitTime;


    public WaitingList() {
    }

    public WaitingList(Restaurant restaurant, List<Guest> listOfUsers, int waitTime) {
        this.restaurant = restaurant;
        this.listOfUsers = listOfUsers;
        this.waitTime = waitTime;

    }

    public WaitingList(List<Guest> listOfUsers, int waitTime) {
        this.listOfUsers = listOfUsers;
        this.waitTime = waitTime;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "waitlist")
    @JsonManagedReference
    public List<Guest> getListOfUsers() {
        if (listOfUsers == null) {
            listOfUsers = new ArrayList<>();

        }
        return listOfUsers;
    }

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "foreign", parameters = @Parameter(value = "restaurant", name = "property"))
    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setListOfUsers(List<Guest> listOfUsers) {

        this.listOfUsers = listOfUsers;
    }

    @Column(name = "wait_time")
    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }



}
