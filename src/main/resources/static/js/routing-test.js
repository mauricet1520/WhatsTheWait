var testApp = angular.module("testApp", ["ngRoute"]);

console.log("testApp module created");

testApp.config(function($routeProvider) {
    console.log("Initializing ng-router");
    $routeProvider
        .when("/", {
            templateUrl: "login.html",
            controller: "controller1"

        })

        .when("/restaurants", {
            templateUrl: "restaurants.html",
            controller: "controller2"

        })

        .when("/waitlist", {
            templateUrl: "waitlist.html",
            controller: "controller3"

        })

        .when("/reservations", {
            templateUrl: "reservations.html",
            controller: "controller4"

        })

        .when("/register", {
            templateUrl: "register.html",
            controller: "controller5"

        })

        .when("/employee-register", {
             templateUrl: "employee-register.html",
             controller: "controller6"

        })

        .when("/employee-sign-in", {
              templateUrl: "employee-sign-in.html",
              controller: "controller7"

        })
        .when("/employee-waitlist", {
             templateUrl: "employee-waitlist.html",
             controller: "controller8"

        })

});

testApp.controller('controller1', function($scope, $http, $location, $rootScope) {
    console.log("init controller 1...")

    $scope.login = function() {
         console.log("About to go get me some data!");
         console.log("About to sign in the following guest " + JSON.stringify($scope.currentGuest));

         $http.post("/login_guest.json", $scope.currentGuest)
             .then(
                 function successCallback(response) {
                     console.log(response.data);
                     console.log("Adding data to scope");
                     $rootScope.returnedGuest = response.data;
                     $location.path("/restaurants");
                 },

                function errorCallback(response) {
                     console.log("Unable to get data");
                     $location.path("/");
                     alert("Wrong email or password");

                });
    };

$scope.currentGuest = {};
$rootScope.selectedFirstName = {};
$rootScope.returnedGuest = {};

});

testApp.controller('controller2', function($scope, $http, $location, $rootScope) {
    console.log("init controller 2...")

    $rootScope.getWaitList = function(name){
        console.log("fetching waitlist for "+ name)
        $rootScope.restaurantName = name;
        $location.path("/waitlist");
        $http.get("get_restaurant_waitlist.json?name=" + name)
            .then(
                function successCallback(response) {
                    console.log(response.data);
                    console.log("Adding data to scope");
                    $rootScope.waitlist = response.data;

                },
                function errorCallback(response) {
                    console.log("Unable to get data");
                    alert("You must be signed in or registered");
                    $location.path("/");

                });
    };
    $scope.currentWait = $rootScope.waitlist;

    $rootScope.getRestaurants = function() {
        console.log("fetching restaurants list from server ...");
        $http.get("/get_restaurants.json")
            .then(
                function successCallback(response) {
                    console.log(response.data);
                    console.log("Adding data to scope");
                    $rootScope.restaurantList = response.data;
                },
                function errorCallback(response) {
                console.log("Unable to get data");
        });

    };

$rootScope.restaurantList = {};
$scope.myListItems = [];
$scope.getRestaurants();

});

testApp.controller('controller3', function($scope, $rootScope, $http, $location) {
    console.log("init controller 3...")

    $scope.addGuest = function(){
        console.log("add guest to the wait")
        $scope.currentWaitlist = $rootScope.waitlist
        $scope.returnedWaitlist = $rootScope.getWaitList
        $http.post("/add_guest_to_waitlist.json", $scope.currentWaitlist)
            .then(
                function successCallback(response) {
                     console.log(response.data);
                     console.log("Adding data to scope");
                     $rootScope.waitlist = response.data;
                     $scope.returnedWaitlist($rootScope.restaurantName)
                     $location.path("/waitlist");
                },
                function errorCallback(response) {
                    console.log("Unable to get data");
                    alert("You must sign in or register");
                    $location.path("/");
            });

    };

$scope.list= {};
$scope.currentRestaurant = $rootScope.restaurantName;
});

testApp.controller('controller5', function($scope, $http, $rootScope, $location) {
    console.log("init controller 5...")

    $scope.register = function() {
        console.log("About to go get me some data!");
        console.log("About to register the following guest " + JSON.stringify($scope.currentGuest));

            $http.post("/register_guest.json", $scope.currentGuest)
                .then(
                    function successCallback(response) {
                        console.log(response.data);
                        console.log("Adding data to scope");
                        $rootScope.returnedGuest = response.data;
                        $location.path("/restaurants");
                     },
                    function errorCallback(response) {
                        console.log("Unable to get data");
                         $location.path("/register");

                    });
                };

    $scope.currentGuest = {};

});

testApp.controller('controller6', function($scope, $http, $rootScope, $location) {
    console.log("init controller 6...")

    $scope.registerEmployee = function() {
        console.log("About to go get me some data!");
        console.log("About to register the following employee " + JSON.stringify($scope.employee));

        $http.post("/add_employee.json", $scope.employee)
            .then(
                function successCallback(response) {
                    console.log(response.data);
                    console.log("Adding data to scope");
                    $scope.returnedRestaurant = response.data;
                    $location.path("/employee-waitlist");
                },
                function errorCallback(response) {
                    console.log("Unable to get data");
                    $location.path("/employee-register");
                });
        };

$scope.employee = {};
$scope.returnedRestaurant = {};

      $scope.registerRestaurant = function() {
          console.log("About to go get me some data!");
          console.log("About to add the following restaurant " + JSON.stringify($scope.newRestaurant));

          $http.post("/register_restaurant.json", $scope.newRestaurant)
              .then(
                  function successCallback(response) {
                      console.log(response.data);
                      console.log("Adding data to scope");
                      $scope.returnedNewRestaurant = response.data;
                      $location.path("/restaurants");
                  },
                  function errorCallback(response) {
                      console.log("Unable to get data");
                      $location.path("/employee-sign-in");

          });
      };

$scope.newRestaurant = {};
$scope.returnedNewRestaurant = {};

});

testApp.controller('controller7', function($scope, $http, $rootScope, $location) {
    console.log("init controller 7...")

    $scope.signInEmployee = function() {
        console.log("About to go get me some data!");
        console.log("About to sign in the following employee " + JSON.stringify($scope.restaurantEmployee));

        $http.post("/employee_sign_in.json", $rootScope.restaurantEmployee)
            .then(
                function successCallback(response) {
                    console.log(response.data);
                    console.log("Adding data to scope");
                    $scope.returnedRestaurant = response.data;
                    $location.path("/employee-waitlist");
                },
                function errorCallback(response) {
                    console.log("Unable to get data");
                     $location.path("/employee-sign-in");

                });
    };

$rootScope.restaurantEmployee = {};
$scope.returnedRestaurant = {};


});


testApp.controller('controller8', function($scope, $http, $rootScope, $location) {
    console.log("init controller 8...")

    $scope.employeeAddGuest = function() {
        console.log("About to go get me some data!");
        console.log("About to add in the following guest " + JSON.stringify($scope.newGuest));

        $http.post("/add_guest_from_employee.json", $scope.newGuest)
            .then(
                function successCallback(response) {
                    console.log(response.data);
                    console.log("Adding data to scope");
                    $scope.returnedUpdatedList = response.data;
                   $rootScope.getEmployeeList($rootScope.restaurantNameFromEmployee)
                    $location.path("/employee-waitlist");
                },
                function errorCallback(response) {
                    console.log("Unable to get data");
                     $location.path("/employee-sign-in");

                });
    };

$scope.returnedUpdatedList = {};
$scope.newGuest = {};


     $rootScope.getEmployeeList = function(name){
         console.log("fetching waitlist for "+ name)
         $rootScope.restaurantNameFromEmployee = name;
         $http.get("/get_employee_waitlist.json?name=" + name)
         .then(
             function successCallback(response) {
                 console.log(response.data);
                 console.log("Adding data to scope");
                 $rootScope.employeeWaitlist = response.data;
                 $location.path("/employee-waitlist");

             },
            function errorCallback(response) {
                console.log("Unable to get data");
            });

        };
            $scope.currentWait = $rootScope.employeeWaitlist;


    $scope.deleteGuest = function(name){
            console.log("fetching guest" + name)
//            $rootScope.restaurantNameFromEmployee = name;
//            $location.path("/employee-waitlist");
                $http.post("/delete_guest.json?name=" + name)
                .then(
                    function successCallback(response) {
                        console.log(response.data);
                        console.log("Deleting data from scope");
//                        $rootScope.employeeWaitlist = response.data;
                        $location.path("/employee-waitlist");

                    },
                    function errorCallback(response) {
                        console.log("Unable to get data");
                    });

        };

});


testApp.controller('testController', function($scope, $http, $rootScope) {
    console.log("Initializing testController");
    $scope.testVar = "What's The Wait";

    $scope.getRestaurants = function() {
        console.log("fetching restaurants list from server ...");
        $http.get("/get_restaurants.json")
            .then(
                function successCallback(response) {
                    console.log(response.data);
                    console.log("Adding data to scope");
                    $scope.restaurantList = response.data;
                },
                function errorCallback(response) {
                    console.log("Unable to get data");
                });

    };

$scope.myguest = $rootScope.returnedGuest;
$scope.restaurantList = {};
$scope.getRestaurants();


});
