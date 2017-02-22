package com.tiy.practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.Date;

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
        WaitingList waitingList = new WaitingList();
        ReservationList reservationList = new ReservationList();
        if (guestRepository.count() == 0) {
            Guest guest = new Guest("Maurice", "Thomas", "mauricet1520@gmail.com", "1520", 0);
            Restaurant restaurant = new Restaurant("Red Lobster", "Seafood", "Buford Ga", "redLob", "redlobster@gmail.com");
            guestRepository.save(guest);
            waitingList.setRestaurant(restaurant);
            restaurant.setWaitingList(waitingList);

            reservationList.setRestaurant(restaurant);
            restaurant.setReservationList(reservationList);

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
            for (int i = 0; i <waitingList.getListOfUsers().size() ; i++) {
                Guest guest = waitingList.getListOfUsers().get(i);
                LocalTime time1 = LocalTime.now();
                Time timeNow = Time.valueOf(time1);
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H m s");
                String text1 = time1.format(formatter1);
                LocalTime parsedDate1 = LocalTime.parse(text1, formatter1);
                guest.setTime_now(timeNow);
                long diff = Math.abs(guest.getTime_now().getTime() - guest.getStart_time().getTime());
                long res = diff/60000;
                guest.setWaiting((int) res);
                waitingList.setWaitTime(waitingList.getWaitTime() + 5);

//                waitingList.setWaitTime(waitingList.getListOfUsers().get(0).getWaiting());

//                guest.setWaiting(guest.getCurrently().compareTo(guest.getAdded()));
//                guest.setWaiting(parsedDate1.getMinute() - guest.getStartTime());
                guestRepository.save(guest);

            }
//            for (Guest guest : waitingList.getListOfUsers()) {
//                waitingList.setWaitTime(waitingList.getWaitTime() + 5);
//            }
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

        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H m s");
        String text = time.format(formatter);
        LocalTime parsedDate = LocalTime.parse(text, formatter);
        Time timeNow = Time.valueOf(time);

        currentGuest.setTime_now(timeNow);
        currentGuest.setStart_time(timeNow);
//        currentGuest.setWaiting(currentGuest.getCurrently().compareTo(currentGuest.getAdded()));
        long diff = Math.abs(currentGuest.getTime_now().getTime() - currentGuest.getStart_time().getTime());
        long res = diff/60000;
        currentGuest.setWaiting((int) res);

//        currentGuest.setWaiting(currentGuest.getTimeStatus() - currentGuest.getStartTime());
//        guestRepository.save(currentGuest);

         waitingList = waitingListRepository.findOne(currentRestaurant.getId());

        if (waitingList == null) {
            List<Guest> guests = new ArrayList<>();
            guests.add(currentGuest);
            waitingList = new WaitingList(currentRestaurant, guests, 0);
            currentGuest.setWaitlist(waitingList);
            currentRestaurant.setWaitingList(waitingList);
        } else {

            List<Guest> guests = new ArrayList<>();
            guests.addAll(waitingList.getListOfUsers());
            guests.add(currentGuest);
            waitingList.setListOfUsers(guests);
//            waitingList.getListOfUsers().add(currentGuest);
            currentGuest.setWaitlist(waitingList);


        }


        waitingList.setWaitTime(0);
        Collections.sort(waitingList.getListOfUsers());


        for (int i = 0; i <waitingList.getListOfUsers().size() ; i++) {
            Guest guest = waitingList.getListOfUsers().get(i);
            LocalTime time1 = LocalTime.now();
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H m s");
            String text1 = time1.format(formatter1);
            LocalTime parsedDate1 = LocalTime.parse(text1, formatter1);
            timeNow = Time.valueOf(time1);

            guest.setTime_now(timeNow);
             diff = Math.abs(guest.getTime_now().getTime() - guest.getStart_time().getTime());
             res = diff/60000;
            guest.setWaiting((int) res);
            waitingList.setWaitTime(waitingList.getWaitTime() + 5);

//            waitingList.setWaitTime(waitingList.getListOfUsers().get(0).getWaiting());

//            guest.setWaiting(guest.getCurrently().compareTo(guest.getAdded()));
//            guest.setWaiting(parsedDate1.getMinute() - guest.getStartTime());
            guestRepository.save(guest);

        }
//        for (Guest guest : waitingList.getListOfUsers()) {
//            waitingList.setWaitTime(waitingList.getWaitTime() + 5);
//            LocalTime time1 = LocalTime.now();
//            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H m s");
//            String text1 = time1.format(formatter1);
//            LocalTime parsedDate1 = LocalTime.parse(text1, formatter1);
//            guest.setWaiting(parsedDate1.getMinute() - guest.getStartTime());
//
//        }

        waitingListRepository.save(waitingList);
        restaurantRepository.save(currentRestaurant);


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

        Collections.sort(waitingList.getListOfUsers());

        for (int i = 0; i <waitingList.getListOfUsers().size() ; i++) {
            Guest guest = waitingList.getListOfUsers().get(i);
            LocalTime time1 = LocalTime.now();
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H m s");
            String text1 = time1.format(formatter1);
            LocalTime parsedDate1 = LocalTime.parse(text1, formatter1);
            Time timeNow = Time.valueOf(time1);

            guest.setTime_now(timeNow);
            long diff = Math.abs(guest.getTime_now().getTime() - guest.getStart_time().getTime());
            long res = diff/60000;
            guest.setWaiting((int) res);
            waitingList.setWaitTime(waitingList.getWaitTime() + 5);

//            waitingList.setWaitTime(waitingList.getListOfUsers().get(0).getWaiting());
//            guest.setWaiting(guest.getCurrently().compareTo(guest.getAdded()));
//            guest.setWaiting(parsedDate1.getMinute() - guest.getStartTime());
            guestRepository.save(guest);

        }


        waitingListRepository.save(waitingList);
        restaurantRepository.save(currentRestaurant);


        return waitingList;
    }

    @RequestMapping(path = "/get_waitlist.json", method = RequestMethod.GET)
    public List<WaitingList> getWaitList() {
        WaitingList waitingList = new WaitingList();

        List<WaitingList> lists = new ArrayList<>();
        Iterable<WaitingList> waitingLists = waitingListRepository.findAll();
        for (WaitingList list : waitingLists) {
            lists.add(list);


            for (int i = 0; i <waitingList.getListOfUsers().size() ; i++) {
                Guest guest = waitingList.getListOfUsers().get(i);
                LocalTime time1 = LocalTime.now();
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H m s");
                String text1 = time1.format(formatter1);
                LocalTime parsedDate1 = LocalTime.parse(text1, formatter1);
                Time timeNow = Time.valueOf(time1);

                guest.setTime_now(timeNow);
                long diff = Math.abs(guest.getTime_now().getTime() - guest.getStart_time().getTime());
                long res = diff/60000;
                guest.setWaiting((int) res);
                waitingList.setWaitTime(waitingList.getWaitTime() + 5);

//                waitingList.setWaitTime(waitingList.getListOfUsers().get(0).getWaiting());

//                guest.setWaiting(guest.getCurrently().compareTo(guest.getAdded()));
//                guest.setWaiting(parsedDate1.getMinute() - guest.getStartTime());
                guestRepository.save(guest);

            }


//            for (Guest guest : waitingList.getListOfUsers()) {
//                waitingList.setWaitTime(waitingList.getWaitTime() + 5);
//                LocalTime time1 = LocalTime.now();
//                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H m s");
//                String text1 = time1.format(formatter1);
//                LocalTime parsedDate1 = LocalTime.parse(text1, formatter1);
//                guest.setWaiting(parsedDate1.getMinute() - guest.getStartTime());
////                guestRepository.save(guest);
//            }
        }

        return lists;
    }

    @RequestMapping(path = "/register_restaurant.json", method = RequestMethod.POST)
    public Restaurant registerRestaurant(@RequestBody RestaurantRequest restaurant) {

        Restaurant registeredRestaurant = new Restaurant(restaurant.getName(), restaurant.getType(),
                restaurant.getAddress(), restaurant.getPassword(), restaurant.getEmail());
        waitingList = new WaitingList(registeredRestaurant, null, 0);
        registeredRestaurant.setWaitingList(waitingList);

        ReservationList reservationList = new ReservationList(registeredRestaurant, null);
        registeredRestaurant.setReservationList(reservationList);

        restaurantRepository.save(registeredRestaurant);
        return registeredRestaurant;
    }

    @RequestMapping(path = "/add_employee.json", method = RequestMethod.POST)
    public Employee addEmployee(HttpSession session, HttpSession httpSession, @RequestBody EmployeeRequest employeeRequest) {

        currentRestaurant = restaurantRepository.findByNameAndPassword(employeeRequest.getName(), employeeRequest.getPassword());
        Employee currentEmployee = new Employee(currentRestaurant, employeeRequest.getFirstName(), employeeRequest.getLastName(),
                employeeRequest.getPosition());

        httpSession.setAttribute("restaurant", currentRestaurant);

        currentRestaurant.getEmployees().add(currentEmployee);
        currentEmployee.setRestaurant(currentRestaurant);
        session.setAttribute("employee", currentEmployee);

        restaurantRepository.save(currentRestaurant);
        return currentEmployee;
    }

    @RequestMapping(path = "/delete_restaurant.json", method = RequestMethod.POST)
    public void deleteRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant myRestaurant = restaurantRepository.findByName(restaurant.getName());
        restaurantRepository.delete(myRestaurant.getId());

    }

    @RequestMapping(path = "/delete_guest.json", method = RequestMethod.POST)
    public void deleteGuest(HttpSession session, HttpSession httpSession) {
        Guest guest = (Guest) httpSession.getAttribute("employeeGuest");
        Employee employee = (Employee) session.getAttribute("employee");
        Restaurant currentRestaurant = employee.getRestaurant();
         waitingList = waitingListRepository.findOne(currentRestaurant.getId());


        waitingList.getListOfUsers().remove(guest);
//        currentRestaurant.getWaitingList().getListOfUsers().remove(guest);
        guest.setWaitlist(null);
        Collections.sort(waitingList.getListOfUsers());



        waitingListRepository.save(waitingList);
        restaurantRepository.save(currentRestaurant);
        guestRepository.save(guest);


    }

    @RequestMapping(path = "/employee_sign_in.json", method = RequestMethod.POST)
    public Employee employeeSign(HttpSession session, HttpSession httpSession, @RequestBody EmployeeRequest employee) throws Exception {
        Employee currentEmployee = employeeRepository.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
        currentRestaurant = restaurantRepository.findByPassword(employee.getPassword());
        httpSession.setAttribute("restaurant", currentRestaurant);

        if (currentEmployee == null && currentRestaurant == null) {
            throw new Exception("Wrong password or email or restaurant");
        }

        session.setAttribute("employee", currentEmployee);
        return currentEmployee;
    }

    @RequestMapping(path = "/add_guest_from_employee.json", method = RequestMethod.POST)
    public List<WaitingList> employeeAddGuest(HttpSession session, HttpSession httpSession, @RequestBody GuestRequest guestRequest) throws Exception {

        List<WaitingList> lists = new ArrayList<>();
        Employee currentEmployee = (Employee) session.getAttribute("employee");
        currentRestaurant = currentEmployee.getRestaurant();

        Guest currentGuest = new Guest(guestRequest.getFirstName(), guestRequest.getLastName(),
                null, null, guestRequest.getPartyof());

        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H m s");
        String text = time.format(formatter);
        LocalTime parsedDate = LocalTime.parse(text, formatter);
        Time timeNow = Time.valueOf(time);

        currentGuest.setTime_now(timeNow);
        currentGuest.setStart_time(timeNow);
        long diff = Math.abs(currentGuest.getTime_now().getTime() - currentGuest.getStart_time().getTime());
        long res = diff/60000;
        currentGuest.setWaiting((int) res);
//        currentGuest.setWaiting(currentGuest.getCurrently().compareTo(currentGuest.getAdded()));

//        currentGuest.setWaiting(currentGuest.getTimeStatus() - currentGuest.getStartTime());
        guestRepository.save(currentGuest);



        waitingList = waitingListRepository.findOne(currentRestaurant.getId());
        Collections.sort(waitingList.getListOfUsers());

        if (waitingList == null) {
            List<Guest> guests = new ArrayList<>();
            guests.add(currentGuest);
            waitingList = new WaitingList(currentRestaurant, guests, 0);
            Collections.sort(waitingList.getListOfUsers());


            currentGuest.setWaitlist(waitingList);
            currentRestaurant.setWaitingList(waitingList);
        } else {
            Collections.sort(waitingList.getListOfUsers());
            waitingList.getListOfUsers().add(currentGuest);
            currentGuest.setWaitlist(waitingList);
        }

        waitingListRepository.save(waitingList);
        restaurantRepository.save(currentRestaurant);

        waitingList.setWaitTime(0);

        Collections.sort(waitingList.getListOfUsers());


        for (int i = 0; i <waitingList.getListOfUsers().size() ; i++) {
            Guest guest = waitingList.getListOfUsers().get(i);
            LocalTime time1 = LocalTime.now();
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H m s");
            String text1 = time1.format(formatter1);
            LocalTime parsedDate1 = LocalTime.parse(text1, formatter1);
            timeNow = Time.valueOf(time1);

            guest.setTime_now(timeNow);
            guest.setWaiting(guest.getTime_now().compareTo(guest.getStart_time()));
            waitingList.setWaitTime(waitingList.getWaitTime() + 5);

//            waitingList.setWaitTime(waitingList.getListOfUsers().get(0).getWaiting());

//            guest.setWaiting(parsedDate1.getMinute() - guest.getStartTime());
            guestRepository.save(guest);

        }
//        for (Guest guest : waitingList.getListOfUsers()) {
//            waitingList.setWaitTime(waitingList.getWaitTime() + 5);
//            LocalTime time1 = LocalTime.now();
//            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H m s");
//            String text1 = time1.format(formatter1);
//            LocalTime parsedDate1 = LocalTime.parse(text1, formatter1);
//            guest.setWaiting(parsedDate1.getMinute() - guest.getStartTime());
//            guestRepository.save(guest);
//        }

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

    @RequestMapping(path = "/sign_out_guest.json", method = RequestMethod.POST)
    public void signOutGuest(HttpSession session) throws Exception {
       if (session == session.getAttribute("guest")){
           session = null;
       }else if (session == session.getAttribute("employee")){
           session = null;
       }
    }

    @RequestMapping(path = "/edit_guest.json", method = RequestMethod.GET)
        public Guest editGuest(String name, HttpSession session, HttpSession httpSession) {
            Employee employee = (Employee) session.getAttribute("employee");
            currentRestaurant = employee.getRestaurant();
             waitingList = waitingListRepository.findOne(currentRestaurant.getId());
            Guest guest = guestRepository.findByFirstName(name);
            httpSession.setAttribute("employeeGuest", guest);
            return guest;

    }

    @RequestMapping(path = "/update_guest.json", method = RequestMethod.POST)
    public Guest updateGuest(HttpSession session, HttpSession httpSession, @RequestBody GuestRequest guestRequest) {
        Guest guest = (Guest) httpSession.getAttribute("employeeGuest");
        Employee employee = (Employee) session.getAttribute("employee");
        Restaurant currentRestaurant = employee.getRestaurant();
         waitingList = waitingListRepository.findOne(currentRestaurant.getId());

        guest.setPartyof(guestRequest.getPartyof());
        guest.setFirstName(guestRequest.getFirstName());
        guest.setLastName(guestRequest.getLastName());

        waitingListRepository.save(waitingList);
        restaurantRepository.save(currentRestaurant);
        guestRepository.save(guest);

        return guest;

    }

    //json endpoint that gets reservations
    @RequestMapping(path = "/get_restaurant_reservation.json", method = RequestMethod.GET)
    public List<Restaurant> getRestaurantReservation() throws Exception {

        List<Restaurant> restaurantList = new ArrayList<>();

        Iterable<Restaurant> restaurants = restaurantRepository.findAll();
        for (Restaurant list : restaurants) {
            restaurantList.add(list);
        }

        return restaurantList;
    }


    @RequestMapping(path = "/get_reservations.json", method = RequestMethod.GET)
    public ReservationList getReservations(HttpSession restaurantSession, HttpSession session, String name) throws Exception {

        currentRestaurant = restaurantRepository.findByName(name);
        ReservationList reservationList = reservationRepository.findOne(currentRestaurant.getId());
        Guest myGuest = (Guest) session.getAttribute("guest");
        restaurantSession.setAttribute("restaurant", currentRestaurant);

        if (myGuest == null) {
            throw new Exception("User not signed in or registered");
        }


        return reservationList;
    }

    @RequestMapping(path = "/add_reservation.json", method = RequestMethod.POST)
    public ReservationList addReservation( HttpSession restaurantSession, HttpSession session,
                                           @RequestBody GuestRequest guest) throws Exception {

        currentRestaurant = (Restaurant) restaurantSession.getAttribute("restaurant");
        Guest myGuest = (Guest) session.getAttribute("guest");
        if (myGuest == null) {
            throw new Exception("User not signed in or registered");
        }
        ReservationList reservationList = reservationRepository.findOne(currentRestaurant.getId());

        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd-yyyy");
        java.util.Date date = sdf2.parse(guest.getForDate());
        myGuest.setReservationDate(date);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("H m");

        LocalTime localTime = LocalTime.parse(guest.getForTime());

        int anotherTime = localTime.getHour();
        int anotherMinute = localTime.getMinute();
        LocalTime localTime1 = LocalTime.of(anotherTime, anotherMinute);

        myGuest.setReservationTime(Time.valueOf(localTime1));

        myGuest.setPartyof(guest.getPartyof());

        reservationList.getListOfGuests().add(myGuest);
        myGuest.setReservationList(reservationList);

        reservationRepository.save(reservationList);
        restaurantRepository.save(currentRestaurant);

        return reservationList;
    }


}
