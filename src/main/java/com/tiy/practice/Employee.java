package com.tiy.practice;

import javax.persistence.*;

/**
 * Created by crci1 on 2/9/2017.
 */

@Entity
public class Employee {
    private Long id;
    private Restaurant restaurant;
    private String employeeName;

    public Employee() {
    }

    public Employee(Restaurant restaurant, String employeeName) {
        this.restaurant = restaurant;
        this.employeeName = employeeName;
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
    public String getEmployeeName()
    {
        return employeeName;
    }
    public void setEmployeeName(String employeeName)
    {
        this.employeeName = employeeName;
    }

    @ManyToOne(cascade=CascadeType.ALL)
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
