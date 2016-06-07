(function() {
    'use strict';

    angular
        .module('tlcrmspaApp')
        .controller('SupplierDetailController', SupplierDetailController);

    SupplierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Supplier'];

    function SupplierDetailController($scope, $rootScope, $stateParams, entity, Supplier) {
        var vm = this;
        vm.supplier = entity;
        vm.load = function (id) {
            Supplier.get({id: id}, function(result) {
                vm.supplier = result;
            });
        };
        var unsubscribe = $rootScope.$on('tlcrmspaApp:supplierUpdate', function(event, result) {
            vm.supplier = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
