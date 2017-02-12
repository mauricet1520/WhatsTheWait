var testApp = angular.module("testApp", ["ngRoute"]);

console.log("testApp module created");

testApp.config(function($routeProvider) {
    console.log("Initializing ng-router");
    $routeProvider
        .when("/", {
//            template: "<h2>test 1</h2>"
            templateUrl: "login.html",
            controller: "controller1"

        })
        .when("/restaurants", {
        //            template: "<h2>test 1</h2>"
                    templateUrl: "restaurants.html",
                    controller: "controller2"

                })
        .when("/waitlist", {
//            template: "<h2>test 2</h2>"
            templateUrl: "waitlist.html",
            controller: "controller3"

        })
        .when("/reservations", {
//            template: "<h2>test 3</h2>"
            templateUrl: "reservations.html"
//            controller: "controller4"

        })
});

testApp.controller('controller1', function($scope, $http, $location, $rootScope) {
    console.log("init controller 1...")

    $scope.login = function() {
                console.log("About to go get me some data!");
                 console.log("About to sign in the following guest " + JSON.stringify($scope.currentGuest));

                $location.path("/restaurants");
                $http.post("/login_guest.json", $scope.currentGuest)
                    .then(
                        function successCallback(response) {
                            console.log(response.data);
                            console.log("Adding data to scope");
                            $rootScope.returnedGuest = response.data;
                        },
                        function errorCallback(response) {
                            console.log("Unable to get data");
                        });
            };

//            $scope.name = {};

$scope.currentGuest = {};
$rootScope.selectedFirstName = {};
$rootScope.returnedGuest = {};


//    $scope.getRestaurants = function() {
//            console.log("fetching restaurants list from server ...");
//            $http.get("/get_restaurants.json")
//                .then(
//                    function successCallback(response) {
//                        console.log(response.data);
//                        console.log("Adding data to scope");
//                        $scope.restaurantList = response.data;
//                    },
//                    function errorCallback(response) {
//                        console.log("Unable to get data");
//                    });
//
//
//
//        };
//
//        $scope.restaurantList = {};
//
//        $scope.getRestaurants();




});

testApp.controller('controller2', function($scope, $http, $location, $rootScope) {
    console.log("init controller 2...")


    $scope.getWaitList = function(name){
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
                    });

    };


    $scope.getRestaurants = function() {
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
//        $rootScope.restaurantName = {};

        $scope.getRestaurants();
//        $scope.currentWait = $rootScope.waitlist;


});

testApp.controller('controller3', function($scope, $rootScope) {
    console.log("init controller 3...")

    $scope.currentRestaurant = $rootScope.restaurantName;
});




testApp.controller('testController', function($scope, $http) {
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
                            $scope.restaurantList = {};
                            $scope.getRestaurants();


});
