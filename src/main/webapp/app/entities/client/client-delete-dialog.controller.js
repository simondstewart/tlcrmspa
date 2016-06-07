(function() {
    'use strict';

    angular
        .module('tlcrmspaApp')
        .controller('ClientDeleteController',ClientDeleteController);

    ClientDeleteController.$inject = ['$uibModalInstance', 'entity', 'Client'];

    function ClientDeleteController($uibModalInstance, entity, Client) {
        var vm = this;
        vm.client = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Client.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
