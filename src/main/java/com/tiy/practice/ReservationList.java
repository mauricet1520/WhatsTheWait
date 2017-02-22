package com.tiy.practice;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by crci1 on 2/8/2017.
 */
@Entity
public class ReservationList {
    private Long restaurantId;
    private Restaurant restaurant;
    private List<Guest> listOfGuests;

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

    public ReservationList() {
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservationList")
    @JsonManagedReference
    public List<Guest> getListOfGuests() {
        if (listOfGuests == null){
            listOfGuests = new ArrayList<>();
        }
        return listOfGuests;
    }

    public void setListOfGuests(List<Guest> listOfGuests) {
        this.listOfGuests = listOfGuests;
    }


    public ReservationList(Restaurant restaurant, List<Guest> listOfGuests) {
        this.restaurant = restaurant;
        this.listOfGuests = listOfGuests;
    }
}
