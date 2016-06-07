(function() {
    'use strict';

    angular
        .module('tlcrmspaApp')
        .controller('SupplierDialogController', SupplierDialogController);

    SupplierDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Supplier'];

    function SupplierDialogController ($scope, $stateParams, $uibModalInstance, entity, Supplier) {
        var vm = this;
        vm.supplier = entity;
        vm.load = function(id) {
            Supplier.get({id : id}, function(result) {
                vm.supplier = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tlcrmspaApp:supplierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.supplier.id !== null) {
                Supplier.update(vm.supplier, onSaveSuccess, onSaveError);
            } else {
                Supplier.save(vm.supplier, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateCreated = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
