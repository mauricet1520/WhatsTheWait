package com.tiy.practice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.*;

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

    @Autowired
    EmployeeRepository employeeRepository;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRegisterUser() {
        long size = guestRepository.count();

        Guest guest = new Guest("Test", "User", "test-user@gmail.com", "testpassword", 0);
        guestRepository.save(guest);
        assertNotNull(guest.getId());

        long newSize = guestRepository.count();
        assertEquals(size + 1, newSize);
        guestRepository.delete(guest);

        newSize = guestRepository.count();
        assertEquals(size, newSize);
    }

    @Test
    public void testUserLogin() {
        Guest testGuest = new Guest("Maurice", "Thomas", "mauricet1520@gmail.com", "1520");
        testGuest.setPartyof(10);
        guestRepository.save(testGuest);

        Guest retrievedGuest = guestRepository.findByEmailAndPassword(testGuest.getEmail(), testGuest.getPassword());
        assertNotNull(retrievedGuest);
        assertEquals(retrievedGuest.getId(), testGuest.getId());
        assertEquals(retrievedGuest.getEmail(), testGuest.getEmail());
        assertEquals(retrievedGuest.getPartyof(), testGuest.getPartyof());

        guestRepository.delete(testGuest);
    }

    @Test
    public void testRegisterRestaurant() {
        long size = restaurantRepository.count();
        Restaurant restaurant = new Restaurant("Red Lobster", "Seafood", "Buford Ga", "redLob", "redlobster@gmail.com");
        restaurantRepository.save(restaurant);
        assertNotNull(restaurant.getId());
        long newSize = guestRepository.count();
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
        Restaurant restaurant = new Restaurant("Test Restaurant", "Seafood", "Buford Ga", "redLob", "restauranttest@gmail.com");
        Guest firstUser = new Guest("Test", "Tester", "testertest@gmail.com", "1520", 20);
        Guest secondUser = new Guest("Test 2", "Tester", "carlevans@gmail.com", "4040", 2);
        Guest thirdUser = new Guest("Mike", "Smith", "mikesmith@gmail.com", "2020", 6);
        List<Guest> users = new ArrayList<>();
        users.add(firstUser);
        users.add(secondUser);
        users.add(thirdUser);

        WaitingList waitingList = new WaitingList(restaurant, users, 20);

        Iterable<WaitingList> allList = waitingListRepository.findAll();

        restaurant.setWaitingList(waitingList);

        long size = waitingListRepository.count();
//        assertEquals(0, size);

        firstUser.setWaitlist(waitingList);
        secondUser.setWaitlist(waitingList);
        thirdUser.setWaitlist(waitingList);

        waitingListRepository.save(waitingList);
        restaurantRepository.save(restaurant);

        long newSize = waitingListRepository.count();

        assertEquals(size + 1, newSize);
        Guest testedUser = guestRepository.findByEmail(firstUser.getEmail());

        assertNotNull(testedUser.getId());

        restaurantRepository.delete(restaurant);
//        waitingListRepository.delete(waitingList);

    }

//    @Test
//    public void testReservationList() {
//        Restaurant firstRestaurant = new Restaurant("Red Lobster", "Seafood", "Buford Ga", "redLob", "redlobster@gmail.com");
//        Restaurant secondRestaurant = new Restaurant("Pappadeaux", "Seafood", "Norcross Ga", "deaux", "pappadeaux@gmail.com");
//        Restaurant thirdRestaurant = new Restaurant("Bonefish", "American", "Buford Ga", "bonefish", "bonefish@gmail.com");
//        long size = reservationRepository.count();
//
//        assertEquals(0, size);
//
//        ReservationList reservationListOne = new ReservationList("Tom", "Hanks", 40,
//                java.sql.Timestamp.valueOf(LocalDateTime.of(2017, Month.from(Month.MARCH), 20, 7, 5)), firstRestaurant);
//        reservationRepository.save(reservationListOne);
//
//        ReservationList reservationListTwo = new ReservationList("Chris", "Doe", 30,
//                java.sql.Timestamp.valueOf(LocalDateTime.of(2017, Month.from(Month.MARCH), 20, 7, 5)), secondRestaurant);
//        reservationRepository.save(reservationListTwo);
//
//        ReservationList reservationListThree = new ReservationList("Tom", "Hanks", 40,
//                java.sql.Timestamp.valueOf(LocalDateTime.of(2017, Month.from(Month.MARCH), 20, 7, 5)), thirdRestaurant);
//        reservationRepository.save(reservationListThree);
//
//        assertNotNull(reservationListOne.getRestaurantId());
//        assertNotNull(reservationListTwo.getRestaurantId());
//        assertNotNull(reservationListThree.getRestaurantId());
//
//        reservationRepository.delete(reservationListOne);
//        reservationRepository.delete(reservationListTwo);
//        reservationRepository.delete(reservationListThree);
//        restaurantRepository.delete(firstRestaurant);
//        restaurantRepository.delete(secondRestaurant);
//        restaurantRepository.delete(thirdRestaurant);
//
//    }

    @Test
    public void testDeletion() {
        Guest guest = guestRepository.findByEmail("mauricet1520@gmail.com");
        Restaurant restaurant = restaurantRepository.findByPassword("redLob");
        Employee employee = new Employee(restaurant, "Mark", "Lewis", "Manager");
        WaitingList waitingList = waitingListRepository.findOne(restaurant.getId());

        List<Employee> employeeSet = new ArrayList<>();
        employeeSet.add(employee);

        restaurant.setEmployees(employeeSet);
//        restaurant.getEmployees().add(employee);
//        restaurant.setWaitingList(waitingList);

        List<Guest> guestSet = new ArrayList<>();
        guestSet.add(guest);
        waitingList.setListOfUsers(guestSet);
//        waitingList.getListOfUsers().add(guest);
//        restaurant.getWaitingList().getListOfUsers().add(guest);
        guest.setWaitlist(waitingList);


        waitingListRepository.save(waitingList);
        restaurantRepository.save(restaurant);

        long size = guestRepository.count();
        assertNotNull(guest.getId());
        assertEquals(1, size);

        long restaurantSize = restaurantRepository.count();
        long employeeSize = employeeRepository.count();

        assertEquals(1, restaurantSize);
        assertEquals(1, employeeSize);

//        restaurant.getWaitingList().getListOfUsers().add(guest);
//
//        assertEquals(1, restaurant.getWaitingList().getListOfUsers().size());
//
//        waitingList = restaurant.getWaitingList();
//        assertEquals(waitingList.getRestaurantId(), restaurant.getWaitingList().getRestaurantId());


        waitingList.getListOfUsers().remove(guest);
//        restaurant.getWaitingList().getListOfUsers().remove(guest);
        guest.setWaitlist(null);
        waitingListRepository.save(waitingList);
        restaurantRepository.save(restaurant);
        guestRepository.save(guest);

        guestRepository.delete(guest);
//        assertEquals(0, size);
        assertEquals(1, restaurantSize);
        assertEquals(0, waitingList.getListOfUsers().size());

        restaurant.getEmployees().remove(employee);
        employee.setRestaurant(null);
        restaurantRepository.save(restaurant);
        employeeRepository.save(employee);
        employeeRepository.delete(employee);
//        assertEquals(0, employeeSize);
        assertEquals(0, restaurant.getEmployees().size());
//        assertEquals(1, restaurantSize);
//        assertEquals(0, restaurant.getWaitingList().getListOfUsers().size());

    }

    @Test
    public void testDateAndTime(){
        Date date = new Date();
//        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MINUTE_FIELD);
        System.out.println(date + "mydate");
//        System.out.println(dateFormat.format(date));
        LocalTime localTime = LocalTime.now();
        LocalTime localTime1 = LocalTime.of(12, 5);
        int i = localTime.compareTo(localTime1);

        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H m s");
        String text = time.format(formatter);
        LocalTime parsedDate = LocalTime.parse(text, formatter);
        int minute = parsedDate.getMinute();

        LocalTime otherTime = LocalTime.of(2,30);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H m s");
        String text1 = otherTime.format(formatter1);
        LocalTime parsedDate1 = LocalTime.parse(text1, formatter1);
        int minute1 = parsedDate1.getMinute();

        System.out.println(minute - minute1 + " leftover");



//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern();

        System.out.println(minute + " myParse");
        System.out.println(minute1 + " myParse");
//        System.out.println(dateTimeFormatter.format(localTime1));

//        System.out.println(localTime + " Now");
//        System.out.println(localTime1 + " Midnight");
//        System.out.println(i + " my math");
    }

}