package com.tiy.practice;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by crci1 on 2/7/2017.
 */
public interface GuestRepository extends CrudRepository<Guest, Long>{

    Guest findByEmailAndPassword(String email, String password);
    Guest findByEmail(String email);
    Guest findByFirstName(String firstName);

}
