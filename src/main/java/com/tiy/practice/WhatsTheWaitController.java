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

        //hello world

        currentRestaurant = new Restaurant();
        waitingList = new WaitingList();
        if (guestRepository.count() == 0) {
            Guest guest = new Guest("Maurice", "Thomas", "mauricet1520@gmail.com", "1520", 0);
            Restaurant restaurant = new Restaurant("Red Lobster", "Seafood", "Buford Ga", "redLob", "redlobster@gmail.com");
            guestRepository.save(guest);
            waitingList.setRestaurant(restaurant);
            restaurant.setWaitingList(waitingList);
            restaurantRepository.save(restaurant);
        } else {
            System.out.println("DB already initialized!");
        }
    }

    @RequestMapping(path = "/register_guest.json", method = RequestMethod.POST)
    public Guest registerGuest(HttpSession session, @RequestBody GuestRequest guest) {
        Guest currentGuest = guestRepository.findByEmail(guest.getEmail());
        if (currentGuest == null) {
            currentGuest = new Guest(guest.getFirstName(), guest.getLastName(), guest.getEmail(), guest.getPassword(), guest.getPartyof());
            session.setAttribute("guest", currentGuest);
        }

        guestRepository.save(currentGuest);
        return currentGuest;
    }

    @RequestMapping(path = "/login_guest.json", method = RequestMethod.POST)
    public Guest loginGuest(HttpSession session, @RequestBody GuestRequest guest) throws Exception {
        Guest currentGuest = guestRepository.findByEmailAndPassword(guest.getEmail(), guest.getPassword());
        if (currentGuest == null) {
            throw new Exception("Wrong password or email");
        } else {
            currentGuest.setPartyof(guest.getPartyof());
            session.setAttribute("guest", currentGuest);
            guestRepository.save(currentGuest);
        }

        return currentGuest;
    }

    @RequestMapping(path = "/get_restaurants.json", method = RequestMethod.GET)
    public List<Restaurant> getRestaurants(HttpSession session) throws Exception {

        List<Restaurant> restaurantList = new ArrayList<>();

        Iterable<Restaurant> allRestaurants = restaurantRepository.findAll();
        for (Restaurant currentRestaurant : allRestaurants) {
            waitingList = currentRestaurant.getWaitingList();
//            waitingList.setWaitTime(0);
//            for (Guest guest : waitingList.getListOfUsers()) {
//                waitingList.setWaitTime(waitingList.getWaitTime() + 5);
//            }
            restaurantList.add(currentRestaurant);
        }

        return restaurantList;
    }

    @RequestMapping(path = "/add_guest_to_waitlist.json", method = RequestMethod.POST)
    public List<WaitingList> addGuesttoWaitList(HttpSession session, @RequestBody WaitListRequest waitListRequest)
            throws Exception {

        List<WaitingList> lists = new ArrayList<>();
        currentRestaurant = restaurantRepository.findOne(currentRestaurant.getId());

        Guest currentGuest = (Guest) session.getAttribute("guest");
        if (currentGuest == null) {
            // this means the user is not logged in
            // or their session has expired (they've been logged out)
            throw new Exception("You need to log in to be added to a wait list");
        }

        waitingList = waitingListRepository.findOne(currentRestaurant.getId());
        if (waitingList == null) {
            Set<Guest> guests = new HashSet<>();
            guests.add(currentGuest);
            waitingList = new WaitingList(currentRestaurant, guests, 0);
            currentGuest.setWaitlist(waitingList);
            currentRestaurant.setWaitingList(waitingList);
        } else {
            waitingList.getListOfUsers().add(currentGuest);
            currentGuest.setWaitlist(waitingList);
        }

//        Guest testedGuest = currentGuest;
//
//        WaitingList testedWaitlist = waitingListRepository.findOne(waitingList.getRestaurantId());
//
//        if (testedWaitlist.getListOfUsers().contains(testedGuest)){
//            waitingList.setWaitTime(0);
//            for (Guest guest : waitingList.getListOfUsers()) {
//                waitingList.setWaitTime(waitingList.getWaitTime() + 5);
//            }
//            Iterable<WaitingList> waitingLists = waitingListRepository.findAll();
//            for (WaitingList list : waitingLists) {
//                lists.add(list);
//            }
//            return lists;
//
//        }

        waitingListRepository.save(waitingList);
        restaurantRepository.save(currentRestaurant);
        TimeKeeper timeKeeper = new TimeKeeper();
        timeKeeper.start();

        waitingList.setWaitTime(timeKeeper.getSecondsPassed());
//        for (Guest guest : waitingList.getListOfUsers()) {
//            waitingList.setWaitTime(waitingList.getWaitTime() + 5);
//        }
        Iterable<WaitingList> waitingLists = waitingListRepository.findAll();
        for (WaitingList list : waitingLists) {
            lists.add(list);
        }
        return lists;
    }

    @RequestMapping(path = "/get_restaurant_waitlist.json", method = RequestMethod.GET)
    public WaitingList getRestaurantWaitList(HttpSession session, String name) throws Exception {

        currentRestaurant = restaurantRepository.findByName(name);
        waitingList = waitingListRepository.findOne(currentRestaurant.getId());
        Guest myGuest = (Guest) session.getAttribute("guest");

        if (myGuest == null) {
            throw new Exception("User not signed in or registered");
        }

        if (waitingList == null) {

            waitingList.getListOfUsers().add(myGuest);
            waitingList.setRestaurant(currentRestaurant);
            myGuest.setWaitlist(waitingList);
            guestRepository.save(myGuest);
            currentRestaurant.setWaitingList(waitingList);
            waitingListRepository.save(waitingList);
            restaurantRepository.save(currentRestaurant);

        }
        waitingList.getWaitTime();

//        waitingList.setWaitTime(0);
//        for (Guest guest : waitingList.getListOfUsers()) {
//            waitingList.setWaitTime(waitingList.getWaitTime() + 5);
//        }
        return waitingList;
    }

    @RequestMapping(path = "/get_waitlist.json", method = RequestMethod.GET)
    public List<WaitingList> getWaitList() {

        List<WaitingList> lists = new ArrayList<>();
        Iterable<WaitingList> waitingLists = waitingListRepository.findAll();
        for (WaitingList list : waitingLists) {
            lists.add(list);
            for (Guest guest : waitingList.getListOfUsers()) {
                waitingList.setWaitTime(waitingList.getWaitTime() + 5);
            }
        }

        return lists;
    }

    @RequestMapping(path = "/register_restaurant.json", method = RequestMethod.POST)
    public Restaurant registerRestaurant(@RequestBody Restaurant restaurant) {

        WaitingList waitingList;
        Restaurant registeredRestaurant = new Restaurant(restaurant.getName(), restaurant.getType(),
                restaurant.getAddress(), restaurant.getPassword(), restaurant.getEmail());
        waitingList = new WaitingList(registeredRestaurant, null, 0);

//        waitingList.setRestaurant(registeredRestaurant);
        registeredRestaurant.setWaitingList(waitingList);

//        waitingListRepository.save(waitingList);
        restaurantRepository.save(registeredRestaurant);
        return registeredRestaurant;
    }

    @RequestMapping(path = "/add_employee.json", method = RequestMethod.POST)
    public Restaurant addEmployee(@RequestBody EmployeeRequest employeeRequest) {

        Restaurant myRestaurant = restaurantRepository.findByNameAndPassword(employeeRequest.getName(), employeeRequest.getPassword());
        Employee currentEmployee = new Employee(myRestaurant, employeeRequest.getFirstName(), employeeRequest.getLastName(),
                employeeRequest.getPosition());

        myRestaurant.getEmployees().add(currentEmployee);
        currentEmployee.setRestaurant(myRestaurant);

        restaurantRepository.save(myRestaurant);
        return myRestaurant;
    }

    @RequestMapping(path = "/delete_restaurant.json", method = RequestMethod.POST)
    public void deleteRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant myRestaurant = restaurantRepository.findByName(restaurant.getName());
        restaurantRepository.delete(myRestaurant.getId());

    }

    @RequestMapping(path = "/employee_sign_in.json", method = RequestMethod.POST)
    public Restaurant employeeSign(@RequestBody EmployeeRequest employee) {
        Employee currentEmployee = employeeRepository.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
        Restaurant employeeRestaurant = restaurantRepository.findByPassword(employee.getPassword());
        return employeeRestaurant;
    }

}
