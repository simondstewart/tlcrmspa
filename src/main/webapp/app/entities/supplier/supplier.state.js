(function() {
    'use strict';

    angular
        .module('tlcrmspaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('supplier', {
            parent: 'entity',
            url: '/supplier?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Suppliers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/supplier/suppliers.html',
                    controller: 'SupplierController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('supplier-detail', {
            parent: 'entity',
            url: '/supplier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Supplier'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/supplier/supplier-detail.html',
                    controller: 'SupplierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Supplier', function($stateParams, Supplier) {
                    return Supplier.get({id : $stateParams.id});
                }]
            }
        })
        .state('supplier.new', {
            parent: 'supplier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/supplier/supplier-dialog.html',
                    controller: 'SupplierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                dateCreated: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('supplier', null, { reload: true });
                }, function() {
                    $state.go('supplier');
                });
            }]
        })
        .state('supplier.edit', {
            parent: 'supplier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/supplier/supplier-dialog.html',
                    controller: 'SupplierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Supplier', function(Supplier) {
                            return Supplier.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('supplier', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('supplier.delete', {
            parent: 'supplier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/supplier/supplier-delete-dialog.html',
                    controller: 'SupplierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Supplier', function(Supplier) {
                            return Supplier.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('supplier', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
