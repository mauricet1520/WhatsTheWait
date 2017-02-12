package com.tiy.practice;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by crci1 on 2/7/2017.
 */
public interface RestaurantRepository extends CrudRepository<Restaurant, Long>{

    Restaurant findByNameAndPassword(String name, String password);


    Restaurant findByName(String name);
}
