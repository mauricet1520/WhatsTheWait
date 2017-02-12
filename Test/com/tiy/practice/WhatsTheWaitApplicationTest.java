package com.tiy.practice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by crci1 on 2/7/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class WhatsTheWaitApplicationTest {

    @Autowired
    GuestRepository guestRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    WaitingListRepository waitingListRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRegisterUser() {
        Iterable<Guest> allUsers = guestRepository.findAll();
        int size = 0;
        for (Guest guest : allUsers) {
            size++;
        }

        Guest guest = new Guest("Maurice", "Thomas", "mauricet1520@gmail.com", "1520", 0);
        guestRepository.save(guest);
        assertNotNull(guest.getId());

        Iterable<Guest> allTheUsers = guestRepository.findAll();
        int newSize = 0;
        for (Guest guest1 : allTheUsers) {
            newSize++;
        }

        assertEquals(size + 1, newSize);
        guestRepository.delete(guest);

    }

    @Test
    public void testUserLogin() {

        Iterable<Guest> allUsers = guestRepository.findAll();
        int size = 0;
        for (Guest guest : allUsers) {
            size++;
        }
        Guest guest = new Guest("Maurice", "Thomas", "mauricet1520@gmail.com", "1520");
        guestRepository.save(guest);
        Guest testedGuest = guestRepository.findByEmailAndPassword(guest.getEmail(), guest.getPassword());
        testedGuest.setPartyof(10);
//        guest = testedGuest;
        guestRepository.save(testedGuest);

        guest = guestRepository.findOne(guest.getId());
        assertEquals(testedGuest.getId(), guest.getId());
        assertEquals(testedGuest.getFirstName(), guest.getFirstName());

        assertEquals(testedGuest.getPartyof(), guest.getPartyof());

        Iterable<Guest> allTheUsers = guestRepository.findAll();
        int newSize = 0;
        for (Guest guest1 : allTheUsers) {
            newSize++;
        }

        assertEquals(size + 1, newSize);

        guestRepository.delete(guest);

        Iterable<Guest> emptyUsers = guestRepository.findAll();
        int finalSize = 0;
        for (Guest guest1 : allUsers) {
            finalSize++;
        }

        assertEquals(0, finalSize);

    }

    @Test
    public void testRegisterRestaurant() {

        Iterable<Restaurant> allRestaurant = restaurantRepository.findAll();
        int size = 0;
        for (Restaurant restaurantLoop : allRestaurant) {
            size++;
        }

        Restaurant restaurant = new Restaurant("Red Lobster", "Seafood", "Buford Ga", "redLob", "redlobster@gmail.com");
        restaurantRepository.save(restaurant);

        assertNotNull(restaurant.getId());

        Iterable<Restaurant> allTheRestaurant = restaurantRepository.findAll();
        int newSize = 0;
        for (Restaurant restaurant1 : allTheRestaurant) {
            newSize++;
        }

        assertEquals(size + 1, newSize);

        restaurantRepository.delete(restaurant);

    }

    @Test
    public void testEmployeeLogin() {

        Restaurant restaurant = new Restaurant("Red Lobster", "Seafood", "Buford Ga", "redLob", "redlobster@gmail.com");
        restaurantRepository.save(restaurant);
        Restaurant testedRestaurant = restaurantRepository.findByNameAndPassword(restaurant.getName(), restaurant.getPassword());
        restaurantRepository.save(testedRestaurant);
        restaurant = restaurantRepository.findOne(restaurant.getId());
        assertEquals(testedRestaurant.getId(), testedRestaurant.getId());

        restaurantRepository.delete(restaurant);

    }

    @Test
    public void testWaitList() {
        Restaurant restaurant = new Restaurant("Red Lobster", "Seafood", "Buford Ga", "redLob", "redlobster@gmail.com");

        Guest firstUser = new Guest("Maurice", "Thomas", "mauricet1520@gmail.com", "1520", 20);
        Guest secondUser = new Guest("Carl", "Evans", "carlevans@gmail.com", "4040", 2);
        Guest thirdUser = new Guest("Mike", "Smith", "mikesmith@gmail.com", "2020", 6);
        Set<Guest> users = new HashSet<>();
        users.add(firstUser);
        users.add(secondUser);
        users.add(thirdUser);

        WaitingList waitingList = new WaitingList(restaurant, users, 20);

        Iterable<WaitingList> allList = waitingListRepository.findAll();
        int size = 0;
        for (WaitingList list : allList) {
            size++;
        }
        assertEquals(0, size);

        firstUser.setWaitlist(waitingList);
        secondUser.setWaitlist(waitingList);
        thirdUser.setWaitlist(waitingList);
        
        waitingListRepository.save(waitingList);

        Iterable<WaitingList> allTheList = waitingListRepository.findAll();
        int newSize = 0;
        for (WaitingList list : allTheList) {
            newSize++;
        }

        assertEquals(size + 1, newSize);
        Guest testedUser = guestRepository.findByEmail(firstUser.getEmail());

        assertNotNull(testedUser.getId());

        waitingListRepository.delete(waitingList);
        restaurantRepository.delete(restaurant);

    }

    @Test
    public void testReservationList() {
        Restaurant firstRestaurant = new Restaurant("Red Lobster", "Seafood", "Buford Ga", "redLob", "redlobster@gmail.com");
        Restaurant secondRestaurant = new Restaurant("Pappadeaux", "Seafood", "Norcross Ga", "deaux", "pappadeaux@gmail.com");
        Restaurant thirdRestaurant = new Restaurant("Bonefish", "American", "Buford Ga", "bonefish", "bonefish@gmail.com");


        Iterable<ReservationList> allList = reservationRepository.findAll();
        int size = 0;
        for (ReservationList list : allList) {
            size++;
        }
        assertEquals(0, size);

        ReservationList reservationListOne = new ReservationList("Tom", "Hanks", 40,
                java.sql.Timestamp.valueOf(LocalDateTime.of(2017, Month.from(Month.MARCH), 20, 7, 5)), firstRestaurant);
        reservationRepository.save(reservationListOne);

        ReservationList reservationListTwo = new ReservationList("Chris", "Doe", 30,
                java.sql.Timestamp.valueOf(LocalDateTime.of(2017, Month.from(Month.MARCH), 20, 7, 5)), secondRestaurant);
        reservationRepository.save(reservationListTwo);

        ReservationList reservationListThree = new ReservationList("Tom", "Hanks", 40,
                java.sql.Timestamp.valueOf(LocalDateTime.of(2017, Month.from(Month.MARCH), 20, 7, 5)), thirdRestaurant);
        reservationRepository.save(reservationListThree);

        assertNotNull(reservationListOne.getRestaurantId());
        assertNotNull(reservationListTwo.getRestaurantId());
        assertNotNull(reservationListThree.getRestaurantId());

        reservationRepository.delete(reservationListOne);
        reservationRepository.delete(reservationListTwo);
        reservationRepository.delete(reservationListThree);
        restaurantRepository.delete(firstRestaurant);
        restaurantRepository.delete(secondRestaurant);
        restaurantRepository.delete(thirdRestaurant);

    }

}