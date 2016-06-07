(function() {
    'use strict';

    angular
        .module('tlcrmspaApp')
        .controller('ClientDialogController', ClientDialogController);

    ClientDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Client', 'Tag'];

    function ClientDialogController ($scope, $stateParams, $uibModalInstance, entity, Client, Tag) {
        var vm = this;
        vm.client = entity;
        vm.tags = Tag.query();
        vm.load = function(id) {
            Client.get({id : id}, function(result) {
                vm.client = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tlcrmspaApp:clientUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.client.id !== null) {
                Client.update(vm.client, onSaveSuccess, onSaveError);
            } else {
                Client.save(vm.client, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
