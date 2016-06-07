(function() {
    'use strict';

    angular
        .module('tlcrmspaApp')
        .controller('ClientDetailController', ClientDetailController);

    ClientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Client', 'Tag'];

    function ClientDetailController($scope, $rootScope, $stateParams, entity, Client, Tag) {
        var vm = this;
        vm.client = entity;
        vm.load = function (id) {
            Client.get({id: id}, function(result) {
                vm.client = result;
            });
        };
        var unsubscribe = $rootScope.$on('tlcrmspaApp:clientUpdate', function(event, result) {
            vm.client = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
