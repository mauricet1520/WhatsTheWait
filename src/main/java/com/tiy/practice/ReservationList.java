package com.tiy.practice;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by crci1 on 2/8/2017.
 */
@Entity
public class ReservationList {
    private Long restaurantId;
    private String firstName;
    private String lastName;
    private int partySize;
    private java.sql.Timestamp time;
    private Restaurant restaurant;

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "foreign", parameters = @Parameter(value = "restaurant", name = "property"))
    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "restaurant_id")
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ReservationList() {
    }

    public ReservationList(String firstName, String lastName, int partySize, Timestamp time, Restaurant restaurant) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.partySize = partySize;
        this.time = time;
        this.restaurant = restaurant;
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

    @Column(name = "party_size")
    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    @Column(name = "time")
    public java.sql.Timestamp getTime() {
        return time;
    }

    public void setTime(java.sql.Timestamp time) {
        this.time = time;
    }
}
