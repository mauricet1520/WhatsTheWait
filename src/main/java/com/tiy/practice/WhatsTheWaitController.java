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
        //Add the following guest and restaurant
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
            waitingList.setWaitTime(0);
            for (Guest guest : waitingList.getListOfUsers()) {
                waitingList.setWaitTime(waitingList.getWaitTime() + 5);
            }
            restaurantList.add(currentRestaurant);
        }

        return restaurantList;
    }

    @RequestMapping(path = "/add_guest_to_waitlist.json", method = RequestMethod.POST)
    public List<WaitingList> addGuesttoWaitList(HttpSession session)
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

        waitingListRepository.save(waitingList);
        restaurantRepository.save(currentRestaurant);

        waitingList.setWaitTime(0);
        for (Guest guest : waitingList.getListOfUsers()) {
            waitingList.setWaitTime(waitingList.getWaitTime() + 5);
        }
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

        waitingList.setWaitTime(0);
        for (Guest guest : waitingList.getListOfUsers()) {
            waitingList.setWaitTime(waitingList.getWaitTime() + 5);
        }
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

        registeredRestaurant.setWaitingList(waitingList);
        restaurantRepository.save(registeredRestaurant);
        return registeredRestaurant;
    }

    @RequestMapping(path = "/add_employee.json", method = RequestMethod.POST)
    public Restaurant addEmployee(HttpSession session, HttpSession httpSession, @RequestBody EmployeeRequest employeeRequest) {

        currentRestaurant = restaurantRepository.findByNameAndPassword(employeeRequest.getName(), employeeRequest.getPassword());
        Employee currentEmployee = new Employee(currentRestaurant, employeeRequest.getFirstName(), employeeRequest.getLastName(),
                employeeRequest.getPosition());

        httpSession.setAttribute("restaurant", currentRestaurant);

        currentRestaurant.getEmployees().add(currentEmployee);
        currentEmployee.setRestaurant(currentRestaurant);
        session.setAttribute("employee", currentEmployee);

        restaurantRepository.save(currentRestaurant);
        return currentRestaurant;
    }

    @RequestMapping(path = "/delete_restaurant.json", method = RequestMethod.POST)
    public void deleteRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant myRestaurant = restaurantRepository.findByName(restaurant.getName());
        restaurantRepository.delete(myRestaurant.getId());

    }

    @RequestMapping(path = "/delete_guest.json", method = RequestMethod.POST)
    public void deleteGuest(String name) {
        Guest guest = guestRepository.findByFirstName(name);
        guestRepository.delete(guest);
    }

    @RequestMapping(path = "/employee_sign_in.json", method = RequestMethod.POST)
    public Restaurant employeeSign(HttpSession session, HttpSession httpSession, @RequestBody EmployeeRequest employee) throws Exception {
        Employee currentEmployee = employeeRepository.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
        currentRestaurant = restaurantRepository.findByPassword(employee.getPassword());
        httpSession.setAttribute("restaurant", currentRestaurant);

        if (currentEmployee == null && currentRestaurant == null) {
            throw new Exception("Wrong password or email or restaurant");
        }

        session.setAttribute("employee", currentEmployee);
        return currentRestaurant;
    }


    @RequestMapping(path = "/add_guest_from_employee.json", method = RequestMethod.POST)
    public List<WaitingList> employeeAddGuest(HttpSession session, HttpSession httpSession, @RequestBody GuestRequest guestRequest) throws Exception {

        List<WaitingList> lists = new ArrayList<>();
        Employee currentEmployee = (Employee) session.getAttribute("employee");
        currentRestaurant = currentEmployee.getRestaurant();

        Guest currentGuest = new Guest(guestRequest.getFirstName(), guestRequest.getLastName(),
                null, null, guestRequest.getPartyof());

        guestRepository.save(currentGuest);

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

        waitingListRepository.save(waitingList);
        restaurantRepository.save(currentRestaurant);

        waitingList.setWaitTime(0);
        for (Guest guest : waitingList.getListOfUsers()) {
            waitingList.setWaitTime(waitingList.getWaitTime() + 5);
        }

        Iterable<WaitingList> waitingLists = waitingListRepository.findAll();
        for (WaitingList list : waitingLists) {
            lists.add(list);
        }

        return lists;

    }

    @RequestMapping(path = "/get_employee_waitlist.json", method = RequestMethod.GET)
    public WaitingList getEmployeeList(HttpSession session, HttpSession httpSession) throws Exception {

        currentRestaurant = (Restaurant) httpSession.getAttribute("restaurant");
        waitingList = waitingListRepository.findOne(currentRestaurant.getId());

        Employee employee = (Employee) session.getAttribute("employee");

        if (employee == null) {
            throw new Exception("User not signed in or registered");
        }

        return waitingList;
    }

}
