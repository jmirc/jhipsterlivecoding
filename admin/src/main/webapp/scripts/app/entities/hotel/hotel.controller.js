'use strict';

angular.module('adminApp')
    .controller('HotelController', function ($scope, Hotel, ParseLinks) {
        $scope.hotels = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Hotel.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.hotels = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Hotel.update($scope.hotel,
                function () {
                    $scope.loadAll();
                    $('#saveHotelModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Hotel.get({id: id}, function(result) {
                $scope.hotel = result;
                $('#saveHotelModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Hotel.get({id: id}, function(result) {
                $scope.hotel = result;
                $('#deleteHotelConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Hotel.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteHotelConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.hotel = {name: null, description: null, address: null, city: null, postalCode: null, country: null, state: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
