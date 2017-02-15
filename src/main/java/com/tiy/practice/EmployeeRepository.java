package com.tiy.practice;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by crci1 on 2/9/2017.
 */
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    Employee findByFirstNameAndLastName(String firstName, String lastName);
}
