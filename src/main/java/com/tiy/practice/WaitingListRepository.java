package com.tiy.practice;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by crci1 on 2/8/2017.
 */
public interface WaitingListRepository extends CrudRepository<WaitingList, Long> {
//    List<WaitingList> findAll(Sort sort);



}
