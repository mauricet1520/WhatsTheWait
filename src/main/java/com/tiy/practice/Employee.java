package com.tiy.practice;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by crci1 on 2/9/2017.
 */

@Entity
public class Employee {
    private Long id;
    private Restaurant restaurant;
    private String firstName;
    private String lastName;
    private String position;

    public Employee() {
    }

    public Employee(Restaurant restaurant, String firstName) {
        this.restaurant = restaurant;
        this.firstName = firstName;
    }

    public Employee(Restaurant restaurant, String firstName, String lastName, String position) {
        this.restaurant = restaurant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @ManyToOne(cascade=CascadeType.ALL)
    @JsonBackReference
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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
}
