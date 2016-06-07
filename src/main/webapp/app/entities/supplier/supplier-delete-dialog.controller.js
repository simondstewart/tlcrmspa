(function() {
    'use strict';

    angular
        .module('tlcrmspaApp')
        .controller('SupplierDeleteController',SupplierDeleteController);

    SupplierDeleteController.$inject = ['$uibModalInstance', 'entity', 'Supplier'];

    function SupplierDeleteController($uibModalInstance, entity, Supplier) {
        var vm = this;
        vm.supplier = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Supplier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
