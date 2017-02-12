package com.tiy.practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by crci1 on 2/9/2017.
 */

@RestController
public class WhatsTheWaitController {

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

    Restaurant currentRestaurant;

    WaitingList waitingList;


    @PostConstruct
    public void initializeDB() {

        currentRestaurant = new Restaurant();
        waitingList = new WaitingList();
        if (guestRepository.count() == 0) {
            Guest guest = new Guest("Maurice", "Thomas", "mauricet1520@gmail.com", "1520", 0);
            Restaurant restaurant = new Restaurant("Red Lobster", "Seafood", "Buford Ga", "redLob", "redlobster@gmail.com");
            guestRepository.save(guest);
            restaurantRepository.save(restaurant);
        } else {
            System.out.println("DB already initialized!");
        }
    }

    @RequestMapping(path = "/register_guest.json", method = RequestMethod.POST)
    public Guest registerGuest(@RequestBody GuestRequest guest) {
        Guest currentGuest = new Guest(guest.getFirstName(), guest.getLastName(), guest.getEmail(), guest.getPassword());
        guestRepository.save(currentGuest);
        return currentGuest;
    }

    @RequestMapping(path = "/login_guest.json", method = RequestMethod.POST)
    public Guest loginGuest(@RequestBody GuestRequest guest) {
        Guest currentGuest = guestRepository.findByEmailAndPassword(guest.getEmail(), guest.getPassword());
        currentGuest.setPartyof(guest.getPartyof());
        guestRepository.save(currentGuest);

        return currentGuest;
    }

    @RequestMapping(path = "/login_user.json", method = RequestMethod.POST)
    public Guest loginGuest(HttpSession httpSession, @RequestBody GuestRequest guest) {
        Guest theGuest = guestRepository.findByEmailAndPassword(guest.getEmail(), guest.getPassword());
//        Guest theGuest = guestRepository.findByFirstName(guest.getFirstName());
        theGuest.setPartyof(guest.getPartyof());
        guestRepository.save(theGuest);

        return theGuest;
    }


    @RequestMapping(path = "/get_restaurants.json", method = RequestMethod.GET)
    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurantList = new ArrayList<>();

        Iterable<Restaurant> allRestaurants = restaurantRepository.findAll();
        for (Restaurant currentRestaurant : allRestaurants) {
            restaurantList.add(currentRestaurant);
        }

        return restaurantList;
    }

    @RequestMapping(path = "/add_guest_to_waitlist.json", method = RequestMethod.POST)
    public List<WaitingList> addGuesttoWaitList(@RequestBody WaitListRequest waitListRequest) {
        List<WaitingList> lists = new ArrayList<>();


        currentRestaurant = restaurantRepository.findByName(waitListRequest.getName());

        waitingList = waitingListRepository.findOne(currentRestaurant.getId());
        Guest currentGuest = guestRepository.findByEmailAndPassword(waitListRequest.getEmail(), waitListRequest.getPassword());


        if (waitingList == null) {
            Set<Guest> guests = new HashSet<>();
            guests.add(currentGuest);
            waitingList = new WaitingList(currentRestaurant, guests, 5);
            currentGuest.setWaitlist(waitingList);
            currentRestaurant.setWaitingList(waitingList);
//            waitingList.setFirstName(currentGuest.getFirstName());
//            waitingList.setPartyOf(currentGuest.getPartyof());
            waitingListRepository.save(waitingList);
            restaurantRepository.save(currentRestaurant);

        } else {
            waitingList.getListOfUsers().add(currentGuest);
            waitingList.setWaitTime(0);
            for (Guest guest : waitingList.getListOfUsers()) {
                waitingList.setWaitTime(waitingList.getWaitTime() + 5);
            }
//            waitingList.setRestaurant(currentRestaurant);
            currentGuest.setWaitlist(waitingList);
//            waitingList.setWaitTime(60);
//            waitingList.setFirstName(currentGuest.getFirstName());
//            waitingList.setPartyOf(currentGuest.getPartyof());
            waitingListRepository.save(waitingList);
            restaurantRepository.save(currentRestaurant);


        }
        Iterable<WaitingList> waitingLists = waitingListRepository.findAll();
        for (WaitingList list : waitingLists) {
            lists.add(list);

        }
        return lists;
    }

    @RequestMapping(path = "/get_restaurant_waitlist.json", method = RequestMethod.GET)
    public WaitingList getRestaurantWaitList(String name) {

        currentRestaurant = restaurantRepository.findByName(name);
        waitingList = waitingListRepository.findOne(currentRestaurant.getId());

        if (waitingList == null) {
            waitingList = new WaitingList(currentRestaurant, null, 0);
            restaurantRepository.save(currentRestaurant);
//            waitingListRepository.save(waitingList);
        }
        return waitingList;
    }

    @RequestMapping(path = "/get_waitlist.json", method = RequestMethod.GET)
    public List<WaitingList> getWaitList() {

//        Restaurant restaurant = new Restaurant("Pappadeaux", "Seafood", "Norcross, Ga", "deaux",
//                "pappas@gmail.com", 30);

        List<WaitingList> lists = new ArrayList<>();

//        Guest firstUser = new Guest("Rachael", "Thomas", "rachael1520@gmail.com", "1520", 30043, 20);
//        Guest secondUser = new Guest("Carl", "Evans", "carlevans@gmail.com", "4040", 30519, 2);
//        Guest thirdUser = new Guest("Mike", "Smith", "mikesmith@gmail.com", "2020", 30509, 6);
//        Set<Guest> users = new HashSet<>();
//        users.add(firstUser);
//        users.add(secondUser);
//        users.add(thirdUser);
//
//        WaitingList waitingList = new WaitingList(restaurant, users, 20);

//        waitingListRepository.save(waitingList);

        Iterable<WaitingList> waitingLists = waitingListRepository.findAll();
        for (WaitingList list : waitingLists) {
            lists.add(list);
        }

        return lists;
    }

    @RequestMapping(path = "/register_restaurant.json", method = RequestMethod.POST)
    public Restaurant registerRestaurant(@RequestBody Restaurant restaurant) {

        Restaurant registeredRestaurant = new Restaurant(restaurant.getName(), restaurant.getType(),
                restaurant.getAddress(), restaurant.getPassword(), restaurant.getEmail());

        restaurantRepository.save(registeredRestaurant);
        return registeredRestaurant;
    }

    @RequestMapping(path = "/add_employee.json", method = RequestMethod.POST)
    public Restaurant addEmployee(@RequestBody EmployeeRequest employeeRequest) {

        Restaurant myRestaurant = restaurantRepository.findByName(employeeRequest.getName());
        Employee currentEmployee = new Employee(myRestaurant, employeeRequest.getEmployeeName());
        myRestaurant.getEmployees().add(currentEmployee);
        currentEmployee.setRestaurant(myRestaurant);

        restaurantRepository.save(myRestaurant);
        return myRestaurant;
    }


}
