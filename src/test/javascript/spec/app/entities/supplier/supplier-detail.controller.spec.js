'use strict';

describe('Controller Tests', function() {

    describe('Supplier Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSupplier;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSupplier = jasmine.createSpy('MockSupplier');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Supplier': MockSupplier
            };
            createController = function() {
                $injector.get('$controller')("SupplierDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tlcrmspaApp:supplierUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
